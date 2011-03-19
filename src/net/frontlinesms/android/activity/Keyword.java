/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2010, Meta Healthcare Systems Ltd.
 *
 * This file is part of FrontlineSMS for Android.
 *
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.android.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import net.frontlinesms.android.FrontlineSMS;
import net.frontlinesms.android.R;
import net.frontlinesms.android.model.model.KeywordAction;
import net.frontlinesms.android.model.model.KeywordActionDao;
import net.frontlinesms.android.ui.view.ActionBar;

/**
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
 */
public class Keyword extends BaseActivity {

    private KeywordAction mKeywordAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword);

        if (getIntent()!=null) {
            int id = getIntent().getIntExtra(FrontlineSMS.EXTRA_KEYWORD_ID, -1);
            if (id!=-1) {
                KeywordActionDao keywordDao = new KeywordActionDao(getContentResolver());
                mKeywordAction = keywordDao.getKeywordById(id);
            }
        }
        initView();
    }

    private void initView() {
        // set title
        if (mKeywordAction==null) {
            ((TextView)findViewById(R.id.txt_header)).setText("New Keyword");
        } else {
            ((TextView)findViewById(R.id.txt_header)).setText("Keyword: " + mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_keyword)).setText(mKeywordAction.getKeyword());
            ((TextView)findViewById(R.id.edt_description)).setText(mKeywordAction.getDescription());
            ((TextView)findViewById(R.id.edt_autoreply)).setText(mKeywordAction.getText());

            ((CheckBox)findViewById(R.id.chk_autoreply)).setChecked(mKeywordAction.getType()==KeywordAction.Type.REPLY);
            ((CheckBox)findViewById(R.id.chk_add_to_group)).setChecked(mKeywordAction.getType()==KeywordAction.Type.JOIN);
            ((CheckBox)findViewById(R.id.chk_remove_from_group)).setChecked(mKeywordAction.getType()==KeywordAction.Type.LEAVE);
        }

    }
}
