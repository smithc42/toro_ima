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
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ImaAdsMediaSourceFactory implements AdsMediaSource.MediaSourceFactory {

  private final DataSource.Factory manifestDataSourceFactory;
  private final DataSource.Factory mediaDataSourceFactory;

  public ImaAdsMediaSourceFactory(Context context) {
    manifestDataSourceFactory = new DefaultDataSourceFactory(context, "ima_loader");
    mediaDataSourceFactory =
        new DefaultDataSourceFactory(context, "ima_loader", new DefaultBandwidthMeter());
  }

  @Override public MediaSource createMediaSource(Uri uri, @Nullable Handler handler,
      @Nullable MediaSourceEventListener listener) {
    return buildMediaSource(uri);
  }

  @Override public int[] getSupportedTypes() {
    return new int[] {C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
  }

  private MediaSource buildMediaSource(Uri uri) {
    @C.ContentType int type = Util.inferContentType(uri);
    switch (type) {
      case C.TYPE_DASH:
        return new DashMediaSource.Factory(
            new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
            manifestDataSourceFactory).createMediaSource(uri);
      case C.TYPE_SS:
        return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
            manifestDataSourceFactory).createMediaSource(uri);
      case C.TYPE_HLS:
        return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
      case C.TYPE_OTHER:
        return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
      default:
        throw new IllegalStateException("Unsupported type: " + type);
    }
  }
}
