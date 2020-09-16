package com.big.moviedb.data.remote.response

data class MovieDetails(
        val popularity : Double,
        val vote_count : Int,
        val video : Boolean,
        val poster_path : String,
        val id : Int,
        val adult : Boolean,
        val backdrop_path : String,
        val original_language : String,
        val original_title : String,
        val title : String,
        val vote_average : Double,
        val overview : String,
        val release_date : String,
        val genre_ids : List<Int>

)
//"results": [
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
//            "title": "Batman",
//            "vote_average": 7.2,
//            "overview": "Having witnessed his parents' brutal murder as a child, millionaire philanthropist Bruce Wayne fights crime in Gotham City disguised as Batman, a costumed hero who strikes fear into the hearts of villains. But when a deformed madman who calls himself \"The Joker\" seizes control of Gotham's criminal underworld, Batman must face his most ruthless nemesis ever while protecting both his identity and his love interest, reporter Vicki Vale.",
//            "release_date": "1989-06-23",
//            "genre_ids": [
////                28,
////                14
////            ],
//        }
//    ]