package com.example.apiexamnewsapp.data

import com.example.apiexamnewsapp.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProductsList(): Flow<Result<List<Product>>>
}