/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zion.ui.commit;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.zion.R;
import com.github.zion.core.commit.CommitUtils;
import com.github.zion.ui.StyledText;
import com.github.zion.util.AvatarLoader;
import com.github.zion.util.TypefaceUtils;

import java.util.Collection;

import org.eclipse.egit.github.core.RepositoryCommit;

/**
 * Adapter to display commits
 */
public class CommitListAdapter extends SingleTypeAdapter<RepositoryCommit> {

    private final AvatarLoader avatars;

    /**
     * @param viewId
     * @param inflater
     * @param elements
     * @param avatars
     */
    public CommitListAdapter(int viewId, LayoutInflater inflater,
            Collection<RepositoryCommit> elements, AvatarLoader avatars) {
        super(inflater, viewId);

        this.avatars = avatars;
        setItems(elements);
    }

    @Override
    public long getItemId(int position) {
        String sha = getItem(position).getSha();
        if (!TextUtils.isEmpty(sha))
            return sha.hashCode();
        else
            return super.getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_commit_id, R.id.tv_commit_author, R.id.iv_avatar,
                R.id.tv_commit_message, R.id.tv_commit_comments };
    }

    @Override
    protected View initialize(View view) {
        view = super.initialize(view);

        TextView commentIcon = (TextView) view .findViewById(R.id.tv_comment_icon);
        commentIcon.setText(TypefaceUtils.ICON_COMMENT);
        TypefaceUtils.setOcticons(commentIcon);
        return view;
    }

    @Override
    protected void update(int position, RepositoryCommit item) {
        setText(0, CommitUtils.abbreviate(item.getSha()));

        StyledText authorText = new StyledText();
        authorText.bold(CommitUtils.getAuthor(item));
        authorText.append(' ');
        authorText.append(CommitUtils.getAuthorDate(item));
        setText(1, authorText);

        CommitUtils.bindAuthor(item, avatars, imageView(2));
        setText(3, item.getCommit().getMessage());
        setText(4, CommitUtils.getCommentCount(item));
    }
}
