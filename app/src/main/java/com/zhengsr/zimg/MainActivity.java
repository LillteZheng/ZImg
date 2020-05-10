package com.zhengsr.zimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhengsr.zimglib.Zimg;
import com.zhengsr.zimglib.cache.DiskLruCache;
import com.zhengsr.zimglib.util.LggUtils;
import com.zhengsr.zimglib.util.ZUtils;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private final int DISK_CACHE_INDEX = 0;
    private DiskLruCache mDiskLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.img);
       /* Glide.with(this)
                .load("http://p1.pstatp.com/large/166200019850062839d3")
                .asBitmap()
                .placeholder(R.mipmap.load)
                .error(R.mipmap.fail)
                .into(imageView);
*/

       /*
       *
       * Picasso的总体流程：
        总的来说Picasso的流程很简单，当Picasso通过load方法获取图片的时候，需要经过如下步骤才能完成显示图片的流程：
            1）将请求封装为Request对象，然后将Request对象进一步封装为Action（ImageAction)对象。
            2）将Action(ImageAction)对象交给Dispather进行分发
            3）最终将action交给BitmapHunter这个Runnable作为在线程池中线程的工作的单元（具体的是讲action持有的当前Reqeuest对象）
            4）由RequestHandler来处理当前request,调用其load方法将加载完成后的图片交给PicassoDrawable显示图片。
        代码流程如下：Picasso->load->创建request->创建action->Dispatcher分发action->RequestHandler的load方法处理具体的请求->PicassoDrawable显示图片。
       * */
        Picasso.get()
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.load)
                .error(R.mipmap.fail)
                .into(imageView);


        Zimg.with(this)
                .load(R.mipmap.ic_launcher)
                .placehoder(R.mipmap.load)
                .error(R.mipmap.fail)
                .into(imageView);

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                downloadAndSave("http://p1.pstatp.com/large/166200019850062839d3");
            }
        }).start();*/




    }

    private Bitmap getBitmap(String url,int reWidth,int reHeight){
        String key = hashKeyFromString(url);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {

                //此时得到的是一个有序的 inputStream 流，
                FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                /**
                 * 如果再把该stream进行第二次 decodeStream，节点已经到末尾了，拿到的数据为 null
                 * 所以，可以通过 inputStream.getFD()拿到FileDescriptor，再去变换。
                 */
                FileDescriptor fileDescriptor = inputStream.getFD();
                BitmapFactory.Options options = new BitmapFactory.Options();
                BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
                options.inJustDecodeBounds = true;
                options.inSampleSize = calculateInSampleSize(options,reWidth,reHeight);
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载和保存，记得放线程中去下载
     * @param url
     */
    private void downloadAndSave(String url){
        String key = hashKeyFromString(url);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);

            if (downloadUrl(url,outputStream)){
                //提交
                editor.commit();

            }else{
                //取消
                editor.abort();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载数据
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadUrl(String urlString, OutputStream outputStream){
        HttpURLConnection con = null;
        BufferedOutputStream out = null;

        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            InputStream stream = con.getInputStream();
            out = new BufferedOutputStream(outputStream);
            int len;
            byte[] bytes = new byte[1024];
            while( (len = stream.read(bytes)) != -1){
                out.write(bytes,0,len);
            }

            return true;

        } catch (Exception e) {
            LggUtils.d("MainActivity-downloadUrl:" +e);
            return false;
        }finally {
            if (con != null) {
                con.disconnect();
            }
            ZUtils.close(out);
        }
    }


    /**
     * 字符串转md5
     * @param key
     * @return
     */
    private String hashKeyFromString(String key) {
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());

            cacheKey = bytesToHexString(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

    /**
     * 数组转字符串
     * @param bytes
     * @return
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }

        return sb.toString();
    }

    /**
     * 计算缩放比例
     * @param options
     * @param reWidth
     * @param reHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reWidth, int reHeight) {
        int w = options.outWidth;
        int h = options.outHeight;
        int inSampleSize = 1;
        if (w > reWidth || h > reHeight) {
            int halfWidth = w / 2;
            int halfHeight = h / 2;
            while ((halfHeight / inSampleSize) >= reHeight &&
                    (halfWidth / inSampleSize) >= reWidth) {
                //2的倍数
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
