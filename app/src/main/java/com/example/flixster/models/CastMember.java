package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CastMember {

    String name;
    String character;
    String profilePath;

    public CastMember(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        character = jsonObject.getString("character");
        profilePath = jsonObject.getString("profile_path");
    }

    public static List<CastMember> fromJsonArray(JSONArray castJsonArray) throws JSONException {
        List<CastMember> cast = new ArrayList<CastMember>();
        for (int i = 0; i < castJsonArray.length(); i++){
            cast.add(new CastMember(castJsonArray.getJSONObject(i)));
        }
        return cast;
    }

    public String getName() {
        return name.replaceAll("\\s+","\n");
    }

    public String getCharacter() {
        return character;
    }

    public String getProfilePath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", profilePath);
    }


}
