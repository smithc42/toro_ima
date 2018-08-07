/*
 * Copyright (c) 2017 Nam Nguyen, nam@ene.im
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

package im.ene.toro.sample;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import com.squareup.leakcanary.LeakCanary;
import im.ene.toro.exoplayer.ToroExo;

/**
 * @author eneim | 6/5/17.
 */

public class ToroDemo extends MultiDexApplication {

  private static ToroDemo singleton;

  @Override public void onCreate() {
    super.onCreate();
    singleton = this;
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
  }

  public static ToroDemo getApp() {
    return singleton;
  }

  @Override public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    if (level >= TRIM_MEMORY_BACKGROUND) ToroExo.with(this).cleanUp();
  }
}
