package com.zhengsr.zimg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zhengsr.zimglib.Zimg;

public class MainActivity extends AppCompatActivity {

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
      /*  Picasso.get()
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.load)
                .error(R.mipmap.fail)
                .into(imageView);*/


        Zimg.with(this)
                .load(R.mipmap.ic_launcher)
                .placehoder(R.mipmap.load)
                .into(imageView);

    }
}
