package com.big.moviedb.data.remote.response

data class MovieResults(

        val page: Int,
        val total_results: Int,
        val total_pages: Int,
        val results: List<MovieDetails>

//{
//    "page": 1,
//    "total_results": 122,
//    "total_pages": 7,
//    "results": [
//        {
//            "popularity": 45.42,
//            "vote_count": 4993,
//            "video": false,
//            "poster_path": "/lH3dsbxA4wLalWKNYpVldX8zfPP.jpg",
//            "id": 268,
//            "adult": false,
//            "backdrop_path": "/2va32apQP97gvUxaMnL5wYt4CRB.jpg",
//            "original_language": "en",
//            "original_title": "Batman",
//            "genre_ids": [
//                28,
//                14
//            ],
//            "title": "Batman",
//            "vote_average": 7.2,
//            "overview": "Having witnessed his parents' brutal murder as a child, millionaire philanthropist Bruce Wayne fights crime in Gotham City disguised as Batman, a costumed hero who strikes fear into the hearts of villains. But when a deformed madman who calls himself \"The Joker\" seizes control of Gotham's criminal underworld, Batman must face his most ruthless nemesis ever while protecting both his identity and his love interest, reporter Vicki Vale.",
//            "release_date": "1989-06-23"
//        }
//    ]
//}
)
