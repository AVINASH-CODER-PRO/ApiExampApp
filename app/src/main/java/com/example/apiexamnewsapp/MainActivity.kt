package com.example.apiexamnewsapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.apiexamnewsapp.data.ProductsRepositoryImpl
import com.example.apiexamnewsapp.data.model.Product
import com.example.apiexamnewsapp.prersentation.ProductsViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<ProductsViewModel>( factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(ProductsRepositoryImpl(RetrofitInstance.api))
                        as T
            }
        }
    }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))) {
                val productList = viewModel.products.collectAsState().value
                val context = LocalContext.current

                LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                    viewModel.showErrorToastChannel.collectLatest { show ->
                        if (show) {
                            Toast.makeText(
                                context, "Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                if (productList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val windowSize = rememberWindowSize()

                    Column {
                        Text(
                            modifier = Modifier.padding(top = 33.dp, start = 16.dp, end = 16.dp).align(Alignment.CenterHorizontally),
                            text = "Products \uD83D\uDCE6 \uD83C\uDF4E ",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        when (windowSize.height) {
                            WindowType.LARGE -> {
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    columns = GridCells.Adaptive(400.dp),
                                ) {
                                    items(productList.size) { index ->
                                        BigScreenProduct(productList[index])
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            }

                            WindowType.SMALL -> {
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    columns = GridCells.Adaptive(300.dp),
                                ) {
                                    items(productList.size) { index ->
                                        SmallScreenProduct(productList[index])
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            }
                            WindowType.MEDIUM -> {
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    columns = GridCells.Adaptive(400.dp),
                                ) {
                                    items(productList.size) { index ->
                                        MediumScreenProduct(productList[index])
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            }
                        }
                    }
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        contentPadding = PaddingValues(12.dp)
//                    ) {
//                        item {
//                            Text(
//                                modifier = Modifier.padding(top = 19.dp),
//                                text = "Products \uD83D\uDCE6 \uD83C\uDF4E ",
//                                fontSize = 24.sp,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(10.dp))
//                        }
//                        items(productList.size) { index ->
//                            Product(productList[index])
//                            Spacer(modifier = Modifier.height(20.dp))
//                        }
//                    }
                }

            }
        }
    }
}



@Composable
fun Product(product: Product){
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(product.thumbnail)
            .size(Size.ORIGINAL).build()
    ).state

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(325.dp)
            .fillMaxWidth()
            .background(Color(0xFFFF8484))
    ){

        if(imageState is AsyncImagePainter.State.Error){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

        if(imageState is AsyncImagePainter.State.Success){
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(230.dp),
                painter = imageState.painter,
                contentDescription = product.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "${product.title} -- Price: ${product.price}$",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
            )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = product.description,
            fontSize = 13.sp,
        )

    }
}

