package com.example.apiexamnewsapp.data

import coil.network.HttpException
import com.example.apiexamnewsapp.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class ProductsRepositoryImpl (
    private val api : Api
): ProductsRepository{

    override suspend fun getProductsList(): Flow<Result<List<Product>>> {
        return flow{
            val productsFromApi = try {
                api.getProductsList()

            } catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(null,message = "Error in loading products"))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Result.Error(null,message = "Error in loading products"))
                return@flow
            } catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(null,message = "Error in loading products"))
                return@flow
            }

            emit(Result.Success(productsFromApi.products))
        }
    }

}