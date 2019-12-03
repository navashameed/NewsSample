package com.sample.news.model

data class NewsData(
    val title: String?,
    val rows: List<Rows>
)

data class Rows(
    val title: String?,
    val description: String?,
    val imageHref: String?
)