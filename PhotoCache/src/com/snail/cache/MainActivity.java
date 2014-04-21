package com.snail.cache;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.snail.util.SnailCache;
import com.snail.util.ThreadPoolManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String URL1 = "http://d.hiphotos.baidu.com/image/w%3D2048/sign=b0f07f7141a98226b8c12c27bebab801/03087bf40ad162d9a9e79f1813dfa9ec8a13cdad.jpg";
    private static final String URL2 = "http://f.hiphotos.baidu.com/image/w%3D2048/sign=3dbacaa22cf5e0feee188e01685835a8/c8177f3e6709c93dbd6acb4c9e3df8dcd00054f5.jpg";
    private static final String URL3 = "http://b.hiphotos.baidu.com/image/w%3D2048/sign=a832eef4b4fd5266a72b3b149f20962b/8326cffc1e178a8290f89dbff403738da877e8ed.jpg";
    private static final String URL4= "http://b.hiphotos.baidu.com/image/w%3D2048/sign=89638712249759ee4a5067cb86c34216/5ab5c9ea15ce36d354a0602f38f33a87e950b10d.jpg";
    private static final String URL5 = "http://b.hiphotos.baidu.com/image/w%3D2048/sign=e0ca1fbad343ad4ba62e41c0b63a5baf/4bed2e738bd4b31cbef876a485d6277f9f2ff8ed.jpg";
    private static final String URL6 = "http://c.hiphotos.baidu.com/image/w%3D2048/sign=ca01c40d249759ee4a5067cb86c34216/5ab5c9ea15ce36d317c2233038f33a87e850b1e8.jpg";
    private static final String URL7 = "http://a.hiphotos.baidu.com/image/w%3D2048/sign=48d9e73e0ef3d7ca0cf63876c627bf09/b3119313b07eca80b5c399e7932397dda04483e8.jpg";
    private static final String URL8 = "http://e.hiphotos.baidu.com/image/w%3D2048/sign=ec5fd3130bf79052ef1f403e38cbd6ca/c75c10385343fbf27eea0623b27eca8064388fe8.jpg";
    private static final String URL9 = "http://a.hiphotos.baidu.com/image/w%3D2048/sign=7a00d6266f224f4a579974133dcf9152/3bf33a87e950352a0ea785085143fbf2b3118be8.jpg";
    private static final String URL10 = "http://g.hiphotos.baidu.com/image/w%3D2048/sign=1d584e47c88065387beaa313a3e5a044/77c6a7efce1b9d1673b15168f1deb48f8c546441.jpg";
    private static final String URL11 = "http://e.hiphotos.baidu.com/image/w%3D2048/sign=05a5c35457fbb2fb342b5f127b7221a4/37d3d539b6003af366cc1769372ac65c1038b641.jpg";
    private static final String URL12 = "http://f.hiphotos.baidu.com/image/w%3D2048/sign=9f5440306963f6241c5d3e03b37ceaf8/902397dda144ad3434c94c8ed2a20cf431ad8541.jpg";
    private static final String URL13 = "http://f.hiphotos.baidu.com/image/w%3D2048/sign=bfb8c4beeb50352ab1612208677bfaf2/2e2eb9389b504fc250297b0ce7dde71190ef6d41.jpg";
    private static final String URL14 = "http://a.hiphotos.baidu.com/image/w%3D2048/sign=49a1ebf734d3d539c13d08c30ebfe850/203fb80e7bec54e7b69ad117bb389b504fc26a41.jpg";
    private static final String URL15 = "http://a.hiphotos.baidu.com/image/w%3D2048/sign=af619e60272dd42a5f0906ab37035ab5/4a36acaf2edda3cc0492732903e93901213f9241.jpg";
    private static final String URL16 = "http://a.hiphotos.baidu.com/image/w%3D2048/sign=235d931b4d4a20a4311e3bc7a46a9922/3b87e950352ac65c7073ad7af9f2b21193138a41.jpg";
    private static final String URL17 = "http://c.hiphotos.baidu.com/image/w%3D2048/sign=9a73ad7af9f2b211e42e824efeb86438/8435e5dde71190effc8d59d6cc1b9d16fdfa6041.jpg";
    private static final String URL18 = "http://g.hiphotos.baidu.com/image/w%3D2048/sign=f619661b700e0cf3a0f749fb3e7ef31f/faf2b2119313b07eaccb9b010ed7912397dd8c41.jpg";
    private static final String URL19 = "http://b.hiphotos.baidu.com/image/w%3D2048/sign=12122c4b8501a18bf0eb154faa170608/42166d224f4a20a4dfe596a092529822730ed0da.jpg";
    private static final String URL20 = "http://g.hiphotos.baidu.com/image/w%3D2048/sign=eacef1511f950a7b753549c43ee963d9/f31fbe096b63f624cda693648544ebf81b4ca3da.jpg";


    private ThreadPoolManager poolManager;
    private SnailCache cache = null;
    private ListView listView;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.listview);
        List<String> list = new ArrayList<String>();
        list.add(URL1);
        list.add(URL2);
        list.add(URL3);
        list.add(URL4);
        list.add(URL5);
        list.add(URL6);
        list.add(URL7);
        list.add(URL8);
        list.add(URL9);
        list.add(URL10);
        list.add(URL11);
        list.add(URL12);
        list.add(URL13);
        list.add(URL14);
        list.add(URL15);
        list.add(URL16);
        list.add(URL17);
        list.add(URL18);
        list.add(URL19);
        list.add(URL20);

        mAdapter = new MyAdapter(list, this);
        listView.setAdapter(mAdapter);


        poolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_LIFO, 5);

        try {
            cache = new SnailCache(getCacheDir(), 1, 10 * 1024 * 1024);
            poolManager.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class MyAdapter extends BaseAdapter {
        private List<String> urlList;
        private Context mContext;

        MyAdapter(List<String> urlList, Context mContext) {
            this.urlList = urlList;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public Object getItem(int position) {
            return urlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            poolManager.loadIcon(holder.icon, urlList.get(position), cache);

            return convertView;
        }
    }

    class ViewHolder {
        ImageView icon;
    }

}
