package com.example.memoryofagoldfishricardsmasniks.volley;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.memoryofagoldfishricardsmasniks.models.Tile;

public class CustomImageRequest  implements Response.Listener<Bitmap>, Response.ErrorListener
{
    private final VolleyImageResponse mVolleyImageResponse;
    private final Tile mTile;
    private final ImageRequest mImageRequest;


    public CustomImageRequest(String pUrl,
                                  Tile pTile,
                                  VolleyImageResponse pVolleyImageResponse)
    {
        mVolleyImageResponse = pVolleyImageResponse;
        mImageRequest = new ImageRequest(pUrl,
                this,
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                this);
        mTile = pTile;
    }
    @Override
    public void onErrorResponse(VolleyError error)
    {
        mVolleyImageResponse.onError(error, mTile.getName());
    }

    @Override
    public void onResponse(Bitmap response)
    {
        mVolleyImageResponse.onResponse(response, mTile);

    }

    public ImageRequest getImageRequest() {
        return mImageRequest;
    }
}
