/*
 * Copyright (c) 2018 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.toro_exoplayer_ima;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;

import static android.text.TextUtils.isEmpty;
import static com.google.android.exoplayer2.util.Util.inferContentType;

/**
 * @author eneim (2018/01/24).
 * @since 3.4.0
 */

public interface MediaSourceBuilder {

  @NonNull MediaSource buildMediaSource(@NonNull Context context, @NonNull Uri uri,
      @NonNull Uri adUri, @Nullable String fileExt, @Nullable Handler handler,
      @NonNull DataSource.Factory manifestDataSourceFactory,
      @NonNull DataSource.Factory mediaDataSourceFactory,
      @Nullable MediaSourceEventListener listener,
      ImaAdsMediaSourceFactory imaAdsMediaSourceFactory, ImaAdsLoader adsLoader,
      PlayerView playerView);

  MediaSourceBuilder ADVERT = new MediaSourceBuilder() {
    @NonNull @Override
    public MediaSource buildMediaSource(@NonNull Context context, @NonNull Uri uri,
        @NonNull Uri adUri, @Nullable String ext, @Nullable Handler handler,
        @NonNull DataSource.Factory manifestDataSourceFactory,
        @NonNull DataSource.Factory mediaDataSourceFactory, MediaSourceEventListener listener,
        ImaAdsMediaSourceFactory imaAdsMediaSourceFactory, ImaAdsLoader adsLoader,
        PlayerView playerView) {
      @ContentType int type = isEmpty(ext) ? inferContentType(uri) : inferContentType("." + ext);

      MediaSource mediaSource = null;
      switch (type) {
        case C.TYPE_SS:
          mediaSource = new SsMediaSource.Factory( //
              new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)//
              .createMediaSource(uri, handler, listener);
          break;
        case C.TYPE_DASH:
          mediaSource = new DashMediaSource.Factory(
              new DefaultDashChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
              .createMediaSource(uri, handler, listener);
          break;
        case C.TYPE_HLS:
          mediaSource = new HlsMediaSource.Factory(mediaDataSourceFactory) //
              .createMediaSource(uri, handler, listener);
          break;
        case C.TYPE_OTHER:
          mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory) //
              .createMediaSource(uri, handler, listener);
          break;
      }

      return new AdsMediaSource(mediaSource,
          /* adMediaSourceFactory= */ imaAdsMediaSourceFactory, adsLoader,
          playerView.getOverlayFrameLayout(),
          /* eventHandler= */ null,
          /* eventListener= */ null);
    }
  };
}
