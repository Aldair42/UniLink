package com.example.unilink.network

import com.example.unilink.models.ApiErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class ApiException(message: String) : Exception(message)

class ApiClient(
    baseUrl: String = DEFAULT_BASE_URL
) {
    @PublishedApi
    internal val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    @PublishedApi
    internal val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    suspend inline fun <reified T> get(
        path: String,
        queryParameters: Map<String, String> = emptyMap()
    ): T {
        val response = client.get(path) {
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
        ensureSuccess(response)
        return response.body()
    }

    suspend inline fun <reified Request, reified Response> post(
        path: String,
        body: Request
    ): Response {
        val response = client.post(path) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        ensureSuccess(response)
        return response.body()
    }

    suspend inline fun <reified Request> postWithoutResponse(
        path: String,
        body: Request
    ) {
        val response = client.post(path) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        ensureSuccess(response)
    }

    suspend inline fun <reified Request> patchWithoutResponse(
        path: String,
        body: Request
    ) {
        val response = client.patch(path) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        ensureSuccess(response)
    }

    suspend fun delete(path: String) {
        val response = client.delete(path)
        ensureSuccess(response)
    }

    suspend fun close() {
        client.close()
    }

    @PublishedApi
    internal suspend fun ensureSuccess(response: HttpResponse) {
        if (!response.status.isSuccess()) {
            val responseText = response.bodyAsText()
            val detail = parseErrorMessage(responseText)
                ?: responseText.ifBlank { response.status.description }
            throw ApiException(detail)
        }
    }

    @PublishedApi
    internal fun parseErrorMessage(responseText: String): String? {
        return try {
            json.decodeFromString<ApiErrorResponse>(responseText).message
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    companion object {
        const val DEFAULT_BASE_URL = "http://10.41.132.111:8080"
    }
}
