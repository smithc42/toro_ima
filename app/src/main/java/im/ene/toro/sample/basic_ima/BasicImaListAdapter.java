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

package im.ene.toro.sample.basic_ima;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicImaListAdapter extends RecyclerView.Adapter<BasicImaPlayerViewHolder> {

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") //
  private MediaList mediaList = new MediaList();

  @NonNull @Override public BasicImaPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(BasicImaPlayerViewHolder.LAYOUT_RES, parent, false);
    return new BasicImaPlayerViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull BasicImaPlayerViewHolder holder, int position) {
    holder.bind(mediaList.get(position));
  }

  @Override public int getItemCount() {
    return mediaList.size();
  }
}
