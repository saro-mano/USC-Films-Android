var express = require('express');
var cors = require('cors')
var app = express();
var router = express.Router();
var request = require('request');
const api_key = "e6f1b17d225b1f7500bdfe2b2d0289df";
const poster_url = "https://image.tmdb.org/t/p/w500";
const backdrop_url = "https://image.tmdb.org/t/p/w780";
const dummy_poster = "https://cinemaone.net/images/movie_placeholder.png";
const dummy_backdrop = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
const axios = require('axios');
app.use(cors())


app.get('/', function(req, res) {
    res.send('Hello World');
})

app.get('/homeContent',async function(req, res){

    //parent dictionary to store all the values
    result = {"currently_playing":[], 
                "popular_movies": [],
                "top_rated_movies" : [],
                "trending_movies" : [],
                "popular_tv" : [],
                "top_rated_tv" : [],
                "trending_tv" : []};
    
    //currently playing API call
    
    req_url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ api_key + "&language=en-US&page=1";
    current_playing_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,6);
    })
    for(i = 0 ; i < current_playing_data.length; i++){
        temp = {};
        temp["id"] = current_playing_data[i].id;
        temp["poster_path"] = backdrop_url + current_playing_data[i].poster_path;
        temp["title"] = current_playing_data[i].title;
        result["currently_playing"].push(temp);
    }

    //popular movies API call

    req_url = "https://api.themoviedb.org/3/movie/popular?api_key=" + api_key + "&language=en- US&page=1";
    popular_movie_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < popular_movie_data.length; i++){
        temp = {};
        temp["id"] = popular_movie_data[i].id;
        if(popular_movie_data[i].poster_path)
            temp["poster_path"] = poster_url + popular_movie_data[i].poster_path;
        else
            temp["poster_path"] = dummy_poster;
        temp["title"] = popular_movie_data[i].title;
        result["popular_movies"].push(temp);
    }

    //top rated movies API call

    req_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key + "&language=en- US&page=1";
    top_rated_movie_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < top_rated_movie_data.length; i++){
        temp = {};
        temp["id"] = top_rated_movie_data[i].id;
        if(top_rated_movie_data[i].poster_path)
            temp["poster_path"] = poster_url + top_rated_movie_data[i].poster_path;
        else
            temp["poster_path"] = dummy_poster;
        temp["title"] = top_rated_movie_data[i].title;
        result["top_rated_movies"].push(temp);
    }

    //treding movies API call

    req_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key + "&language=en- US&page=1";
    trending_movie_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < trending_movie_data.length; i++){
        temp = {};
        temp["id"] = trending_movie_data[i].id;
        if(trending_movie_data[i].poster_path)
            temp["poster_path"] = poster_url + trending_movie_data[i].poster_path;
        else
            temp["poster_path"] = dummy_poster;
        temp["title"] = trending_movie_data[i].title;
        result["trending_movies"].push(temp);
    }

    //popular TV API call

    req_url = "https://api.themoviedb.org/3/tv/popular?api_key=" + api_key +"&language=en- US&page=1";
    popular_tv_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < popular_tv_data.length; i++){
        temp = {};
        temp["id"] = popular_tv_data[i].id;
        if(popular_tv_data[i].poster_path)
            temp["poster_path"] = poster_url + popular_tv_data[i].poster_path;
        else
            temp["poster_path"] = dummy_poster;
        temp["title"] = popular_tv_data[i].name;
        result["popular_tv"].push(temp);
    }

    //top-rated TV shows API call

    req_url = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + api_key +"&language=en- US&page=1";
    top_rated_tv_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < top_rated_tv_data.length; i++){
        temp = {};
        temp["id"] = top_rated_tv_data[i].id;
        if(top_rated_tv_data[i].poster_path)
            temp["poster_path"] = poster_url + top_rated_tv_data[i].poster_path;
        else   
            temp["poster_path"] = dummy_poster;
        temp["title"] = top_rated_tv_data[i].name;
        result["top_rated_tv"].push(temp);
    }

    //trending TV shows API call

    req_url = "https://api.themoviedb.org/3/trending/tv/day?api_key=" + api_key;
    treding_tv_data = await axios.get(req_url).then(function (response) {
        return response.data.results.slice(0,10)
    })
    for(i = 0 ; i < treding_tv_data.length; i++){
        temp = {};
        temp["id"] = treding_tv_data[i].id;
        if(treding_tv_data[i].poster_path)
            temp["poster_path"] = poster_url + treding_tv_data[i].poster_path;
        else
            temp["poster_path"] = dummy_poster;
        temp["title"] = treding_tv_data[i].name;
        result["trending_tv"].push(temp);
    }

    // console.log(result);
    res.send(result);
});




function timeConvert(num) {
    var hours = (num / 60);
    var rhours = Math.floor(hours);
    var minutes = (hours - rhours) * 60;
    var rminutes = Math.round(minutes);
    return rhours + "hrs and " + rminutes + "mins";
}

app.get('/getDetails', async function(req, res){
    result = {};
    var weekday = new Array(7);
    weekday[0] = "Sun";
    weekday[1] = "Mon";
    weekday[2] = "Tue";
    weekday[3] = "Wed";
    weekday[4] = "Thu";
    weekday[5] = "Fri";
    weekday[6] = "Sat";
    const id = req.query.id; 
    const media_type = req.query.media_type;
    if(media_type == "movie"){
        req_url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + api_key + "&language=en- US&page=1";
        movie_data = await axios.get(req_url).then(function (response) {
            return response.data;
        });
        result['title'] = movie_data.title;
        genre_list = [];
        movie_data.genres.forEach(element => {
            genre_list.push(element.name);
        });
        result['genres'] = genre_list.join(", ");
        spoken_languages_list = []
        movie_data.spoken_languages.forEach(element => {
            spoken_languages_list.push(element.english_name);
        });
        result['spoken_languages'] = spoken_languages_list.join(", ");
        result['year'] = movie_data.release_date.split("-")[0];
        result['runtime'] = timeConvert(movie_data.runtime); //should convert this to hours
        result['overview'] = movie_data.overview;
        result['vote_average'] = movie_data.vote_average;
        result['tagline'] = movie_data.tagline;
        result["recommended_mov"] = [];
        result["similar_mov"] = [];
        result["media_type"] = "movie";
        result["id"] = id;
        if(movie_data.backdrop_path)
            result["backdrop_path"] = backdrop_url +  movie_data.backdrop_path;
        else
            result["backdrop_path"] = dummy_backdrop;
        if(movie_data.poster_path)
            result["poster_path"] = poster_url + movie_data.poster_path;
        else{
            result["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
        }
        rec_mov_url = "https://api.themoviedb.org/3/movie/"+ id +"/recommendations?api_key=" + api_key+"&language=en-US&page=1"
        recommeded_movie_data = await axios.get(rec_mov_url).then(function (response) {
            return response.data.results;
        });
        // console.log(recommeded_movie_data);
        for(var i = 0 ; i < recommeded_movie_data.length; i++){
            temp = {};
            temp["id"] = recommeded_movie_data[i].id;
            if(recommeded_movie_data[i].poster_path)
                temp["poster_path"] = poster_url + recommeded_movie_data[i].poster_path;
            else
                temp["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
            temp["title"] = recommeded_movie_data[i].title;
            temp["media_type"] = "movie";
            result["recommended_mov"].push(temp);
        }

        sim_mov_url = "https://api.themoviedb.org/3/movie/" + id + "/similar?api_key="+ api_key +"&languag e=en-US&page=1"
        similar_movie_data = await axios.get(sim_mov_url).then(function (response) {
            return response.data.results;
        });

        for(var i = 0 ; i < similar_movie_data.length; i++){
            temp = {};
            temp["id"] = similar_movie_data[i].id;
            if(similar_movie_data[i].poster_path)
                temp["poster_path"] = poster_url + similar_movie_data[i].poster_path;
            else{
                temp["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
            }
            temp["title"] = similar_movie_data[i].title;
            temp["media_type"] = "movie";
            result["similar_mov"].push(temp);
        }

        //getting trailer:
        video_data_url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key="+ api_key +"&languag e=en-US&page=1"
        video_data = await axios.get(video_data_url).then(function (response) {
            return response.data.results;
        });

        youtube_url = "";
        if(video_data.length != 0){
            for(i = 0 ; i < video_data.length; i++){
                // youtube_url = "https://www.youtube.com/embed/"
                if(video_data[i].type.includes("Trailer")){
                    youtube_url += video_data[i].key;
                    break;
                }
                else if(video_data[i].type.includes("Teaser")){
                    youtube_url += video_data[i].key;
                }
                else{
                    youtube_url += "None";
                }
            }
        }
        else{
            youtube_url += "None";
        }
        result["trailer"] = youtube_url;

        result["movie_cast"] = [];
        movie_cast_url = "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key="+api_key+"&languag e=en-US&page=1"
        movie_cast_data = await axios.get(movie_cast_url).then(function (response) {
            return response.data.cast.slice(0,6);
        });
        for(i = 0 ; i < movie_cast_data.length;  i++){
            if(movie_cast_data[i].profile_path){
                temp = {};
                temp["id"] = movie_cast_data[i].id;
                temp["name"] = movie_cast_data[i].name;
                temp["character"] = movie_cast_data[i].character;
                temp["profile_pic"] = "https://image.tmdb.org/t/p/w500" + movie_cast_data[i].profile_path;
                result["movie_cast"].push(temp);
            }
        }

        result["movie_rev"] = [];
        movie_rev_url = "https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+api_key+"&langua ge=en-US&page=1";
        movie_rev_data = await axios.get(movie_rev_url).then(function (response) {
            return response.data.results.slice(0,3);
        });
        // console.log(movie_rev_data);
        for(i = 0 ; i < movie_rev_data.length;  i++){
            temp = {};
            temp["author"] = movie_rev_data[i].author;
            temp["content"] = movie_rev_data[i].content;
            var review_date = new Date(movie_rev_data[i].created_at);
            var final_time = weekday[review_date.getDay()] + ", " + review_date.toLocaleString('default', { month: 'short' }) + " " + review_date.getDate() + " " + review_date.getFullYear();

            temp["created_at"] = final_time;
            temp["url"] = movie_rev_data[i].url;
            if(movie_rev_data[i].author_details.rating)
                temp["rating"] = Math.floor(movie_rev_data[i].author_details.rating/2); //check for rating whether null..
            else
                temp["rating"] = 0;
            temp["rating"] = temp["rating"] + "/5";
            if(movie_rev_data[i].author_details.avatar_path && movie_rev_data[i].author_details.avatar_path.includes("https")){
                temp["avatar_path"] = movie_rev_data[i].author_details.avatar_path.slice(1);
            }
            else if (movie_rev_data[i].author_details.avatar_path != null){
                temp["avatar_path"] = "https://image.tmdb.org/t/p/original" + movie_rev_data[i].author_details.avatar_path;
            }
            else{
                temp["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
            }
            result["movie_rev"].push(temp);
        }
        // console.log(result);
        res.send(result);
    }
    else if(media_type == "tv"){
        req_url = "https://api.themoviedb.org/3/tv/" + id + "?api_key=" + api_key + "&language=en- US&page=1";
        // console.log(req_url);
        tv_data = await axios.get(req_url).then(function (response) {
            return response.data;
        });
        result['title'] = tv_data.name;
        genre_list = [];
        tv_data.genres.forEach(element => {
            genre_list.push(element.name);
        });
        result['genres'] = genre_list.join(", ");
        spoken_languages_list = []
        tv_data.spoken_languages.forEach(element => {
            spoken_languages_list.push(element.english_name);
        });
        result['spoken_languages'] = spoken_languages_list.join(", ");
        result['year'] = tv_data.first_air_date.split("-")[0];
        result['runtime'] = tv_data.episode_run_time[0] + " mins";
        result['overview'] = tv_data.overview;
        result['vote_average'] = tv_data.vote_average;
        result['tagline'] = tv_data.tagline;
        result["recommended_mov"] = [];
        result["similar_mov"] = [];
        result["media_type"] = "tv";
        result["id"] = id;
        if(tv_data.backdrop_path)
            result["backdrop_path"] = backdrop_url + tv_data.backdrop_path;
        else
            result["backdrop_path"] = dummy_backdrop;
        if(tv_data.poster_path)
            result["poster_path"] = poster_url + tv_data.poster_path;
        else{
            result["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
        }
        rec_tv_url = "https://api.themoviedb.org/3/tv/"+ id +"/recommendations?api_key=" + api_key+"&language=en-US&page=1"
        //checked upto this..
       
        recommeded_tv_data = await axios.get(rec_tv_url).then(function (response) {
            return response.data.results;
        });
        
        
        for(var i = 0 ; i < recommeded_tv_data.length; i++){
            
            temp = {};
            temp["id"] = recommeded_tv_data[i].id;
            if(recommeded_tv_data[i].poster_path)
                temp["poster_path"] = poster_url + recommeded_tv_data[i].poster_path;
            else
                temp["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
            temp["title"] = recommeded_tv_data[i].name;
            temp["media_type"] = "tv";
            result["recommended_mov"].push(temp);
        }

        sim_tv_url = "https://api.themoviedb.org/3/tv/" + id + "/similar?api_key="+ api_key +"&languag e=en-US&page=1"
        similar_tv_data = await axios.get(sim_tv_url).then(function (response) {
            return response.data.results;
        });

        for(var i = 0 ; i < similar_tv_data.length; i++){
            temp = {};
            temp["id"] = similar_tv_data[i].id;
            if(similar_tv_data[i].poster_path)
                temp["poster_path"] = poster_url + similar_tv_data[i].poster_path;
            else{
                temp["poster_path"] = "https://cinemaone.net/images/movie_placeholder.png";
            }
            temp["title"] = similar_tv_data[i].name;
            temp["media_type"] = "tv";
            result["similar_mov"].push(temp);
        }

        //getting trailer:
        video_data_url = "https://api.themoviedb.org/3/tv/" + id + "/videos?api_key="+ api_key +"&languag e=en-US&page=1"
        video_data = await axios.get(video_data_url).then(function (response) {
            return response.data.results;
        });

        youtube_url = "";

        if(video_data.length != 0){
            for(i = 0 ; i < video_data.length; i++){
                // youtube_url = "https://www.youtube.com/embed/"
                
                if(video_data[i].type.includes("Trailer")){
                    youtube_url += video_data[i].key;
                    break;
                }
                else if(video_data[i].type.includes("Teaser")){
                    youtube_url += video_data[i].key;
                }
                else{
                    youtube_url += "None";
                }
            }
        }
        else{
            youtube_url += "None";
        }
        result["trailer"] = youtube_url;

        result["movie_cast"] = [];
        movie_cast_url = "https://api.themoviedb.org/3/tv/" + id + "/credits?api_key="+api_key+"&languag e=en-US&page=1"
        movie_cast_data = await axios.get(movie_cast_url).then(function (response) {
            return response.data.cast.slice(0,6);
        });
        for(i = 0 ; i < movie_cast_data.length;  i++){
            if(movie_cast_data[i].profile_path){
                temp = {};
                temp["id"] = movie_cast_data[i].id;
                temp["name"] = movie_cast_data[i].name;
                temp["character"] = movie_cast_data[i].character;
                temp["profile_pic"] = "https://image.tmdb.org/t/p/w500" + movie_cast_data[i].profile_path;
                result["movie_cast"].push(temp);
            }
        }

        result["movie_rev"] = [];
        movie_rev_url = "https://api.themoviedb.org/3/tv/"+id+"/reviews?api_key="+api_key+"&language=en-US&page=1";
        movie_rev_data = await axios.get(movie_rev_url).then(function (response) {
            return response.data.results.slice(0,3);
        });
        // console.log(movie_rev_data);
        for(i = 0 ; i < movie_rev_data.length;  i++){
            temp = {};
            temp["author"] = movie_rev_data[i].author;
            temp["content"] = movie_rev_data[i].content;
            
            var review_date = new Date(movie_rev_data[i].created_at);
            var final_time = weekday[review_date.getDay()] + ", " + review_date.toLocaleString('default', { month: 'short' }) + " " + review_date.getDate() + " " + review_date.getFullYear();

            temp["created_at"] = final_time;

            temp["url"] = movie_rev_data[i].url;
            if(movie_rev_data[i].author_details.rating){
                temp["rating"] = Math.floor(movie_rev_data[i].author_details.rating/2);
            }
            temp["rating"] = 0;
            temp["rating"] = temp["rating"] + "/5";
            if(movie_rev_data[i].author_details.avatar_path && movie_rev_data[i].author_details.avatar_path.includes("https")){
                temp["avatar_path"] = movie_rev_data[i].author_details.avatar_path.slice(1);
            }
            else if (movie_rev_data[i].author_details.avatar_path != null){
                temp["avatar_path"] = "https://image.tmdb.org/t/p/original" + movie_rev_data[i].author_details.avatar_path;
            }
            else{
                temp["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
            }
            result["movie_rev"].push(temp);
        }
        res.send(result);
    }
});


app.get('/getSearchResults', async function(req,res){
    result = [];
    var inp = req.query.inp;
    // console.log("----------------------------------------------------------------------------------------")
    console.log(inp);
    search_res_url = "https://api.themoviedb.org/3/search/multi?api_key="+api_key+"&language=en-US&query="+inp;
    search_res_data = await axios.get(search_res_url).then(function (response) {
        return response.data.results;
    });
    for(i = 0 ; i < search_res_data.length;  i++){
        if (search_res_data[i].media_type == "movie" && search_res_data[i].backdrop_path){
            temp = {};
            temp['id'] = search_res_data[i].id;
            temp['title'] = search_res_data[i].title;
            if(search_res_data[i].backdrop_path)
                temp['poster_path'] = "https://image.tmdb.org/t/p/original" + search_res_data[i].backdrop_path;
            else{
                temp['poster_path'] = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
            }
            temp['media_type'] = "movie";
            temp['year'] = search_res_data[i].release_date.split("-")[0];
            if(search_res_data[i].vote_average)
                temp["rating"] = Number((search_res_data[i].vote_average/2).toFixed(1)); //check for rating whether null..
            else
                temp["rating"] = 0;
            result.push(temp);
        }
        else if(search_res_data[i].media_type == "tv" && search_res_data[i].backdrop_path){
            temp = {};
            temp['id'] = search_res_data[i].id;
            temp['title'] = search_res_data[i].name;
            if(search_res_data[i].backdrop_path){
                temp['poster_path'] = "https://image.tmdb.org/t/p/original" + search_res_data[i].backdrop_path;
            }
            else{
                temp['poster_path'] = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg"; 
            }
            temp['year'] = search_res_data[i].first_air_date.split("-")[0];

            if(search_res_data[i].vote_average)
                temp["rating"] = Number((search_res_data[i].vote_average/2).toFixed(1)); //check for rating whether null..
            else
                temp["rating"] = 0;
            temp['media_type'] = "tv";
            result.push(temp);
        }
    }
    // console.log(result);
    res.send(result);
})

app.get('/getCastDetails', async function(req, res){
    result = {}
    var id = req.query.id;
    cast_det_url = "https://api.themoviedb.org/3/person/"+id+"?api_key="+api_key+"&language=en- US&page=1";
    cast_det_data = await axios.get(cast_det_url).then(function (response) {
        return response.data;
    });
    if(cast_det_data.birthday)
        result['birthday'] = cast_det_data.birthday;
    if(cast_det_data.place_of_birth)
        result['birth_place'] = cast_det_data.place_of_birth;
    if(cast_det_data.homepage)
        result['website'] = cast_det_data.homepage;
    if(cast_det_data.gender == 1){
        result['gender'] = "Female";
    }
    else if(cast_det_data.gender == 2){
        result['gender'] = "Male";
    }
    else{
        result['gender'] = "undefined";
    }
    result['name'] = cast_det_data.name;
    if(cast_det_data.also_known_as) 
        result['also_know'] = cast_det_data.also_known_as.join(", ");
    if(cast_det_data.known_for_department)
        result['known_for_dept'] = cast_det_data.known_for_department;
    if(cast_det_data.homepage)
        result['homepage'] = cast_det_data.homepage;
    if(cast_det_data.biography)
        result['bio'] = cast_det_data.biography;
    result['profile_path'] = "https://image.tmdb.org/t/p/original" + cast_det_data.profile_path;

    cast_ext_det_url = "https://api.themoviedb.org/3/person/"+id+"/external_ids?api_key="+api_key+"&la nguage=en-US&page=1";
    
    cast_ext_data = await axios.get(cast_ext_det_url).then(function (response) {
        return response.data;
    });
    if(cast_ext_data.imdb_id){
        result['imdb_id'] = "https://www.imdb.com/name/"+cast_ext_data.imdb_id;
    }  
    if(cast_ext_data.facebook_id){
        result['fb_id'] = "https://www.facebook.com/"+cast_ext_data.facebook_id;
    }
    if(cast_ext_data.instagram_id){
        result['insta_id'] = "https://www.instagram.com/"+cast_ext_data.instagram_id;
    }
    if(cast_ext_data.twitter_id){
        result['twitter_id'] = "https://www.twitter.com/"+cast_ext_data.twitter_id;
    }
    res.send(result);
});




var server = app.listen(8080, function() {
    console.log("Backend Application listening at http://localhost:8080")
})


