package com.example.uygulama

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.uygulama.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var basarili=0 //başarılı tıklanmayı yakalamak için tanım yaptık ve başlangıçta 0 verdik
    private var basarisiz=0 //başarısız tıklanmayı yakalamak için tanım yaptık ve başlangıçta 0 verdik
    private var kalanSure:Long = 600000 //kalan süreyi hesaplamak için yazdık 600000 milisayniye 10 dakika yapar
    private var imageAray =ArrayList<ImageView>() //sol sağ üst alt ve ortadaki resimler için imageView tipinde liste oluştururuz
    private var imageAraySol =ArrayList<Int>() //sol taraftaki görseller için liste
    private var imageAraySag =ArrayList<Int>() //sağ taraftaki görseller için liste
    private var imageArayUs =ArrayList<Int>() //üst taraftaki görseller için liste
    private var imageArayAlt =ArrayList<Int>() //alt taraftaki görseller için liste
    private var imageArayOrta =ArrayList<Int>() //orta taraftaki görseller için liste
    private var handler= Handler()
    private var runnable = Runnable {  } //her geçen sürede ne olacağını tanımladık
    private var ses: MediaPlayer? =null //seslerin çalışması için tanımlama yaptık


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        images() //ilk olarak bu fonksiyon çalışır
        showImages() //sonra bu fonksiyon
        timer() //en son bu fonksiyon çalışır
    }

    private fun timer(){
        object:CountDownTimer(600000,1000){
            //600000 milisaniyede başlayacak yani 10 dakika ve 1000 milisaniye yani 1 saniye azalacak 600-599-598 gibi
            override fun onTick(millisUntilFinished: Long) {
                kalanSure=millisUntilFinished

                //onTick ile her saniye ne olacak onu belirleriz ve kalanSure her saniye azalır
            }
            override fun onFinish() {
                //süre bittiği zaman handler kapatırız
                handler.removeCallbacks(runnable)
                println("Biten Başarılı :$basarili")
                println("Biten Başarısız :$basarisiz")

                //for ile imageAray listesindeki tüm ögeleri gizleriz
                for(image in imageAray){
                    image.visibility = View.INVISIBLE
                }
               val intent= Intent(applicationContext,RaporActivity::class.java)//süre bittiğinde rapor sayfasına geçişi yaparız
                intent.putExtra("Basarisiz",basarisiz)
                startActivity(intent)
                finish()
            }
        }.start()
    }
    private fun showImages(){
        val random= Random()
        runnable = Runnable {
            val randomDiger = random.nextInt(4)
            //sol sağ üst alt taraftaki resimlerdeki görseller 4 tane olduğu için 0-4 arasında random sayı üretir
            val randomOrta = random.nextInt(5)
            //ortadaki görsellerde 5 tane resim olduğu için 0-5 arasında random sayı üretir

      /*      for(image in imageAray){
                if(image.visibility==View.VISIBLE){
                    image.visibility=View.INVISIBLE
                }else{
                    image.visibility=View.VISIBLE
                }
            }*/

            binding.imageUst.setImageResource(imageArayUs[randomDiger]) //üstteki görseller random sayıya göre değişir
            binding.imageAlt.setImageResource(imageArayAlt[randomDiger]) //alttaki görseller random sayıya göre değişir
            binding.imageSol.setImageResource(imageAraySol[randomDiger]) //soldaki görseller random sayıya göre değişir
            binding.imageSag.setImageResource(imageAraySag[randomDiger]) //sağdaki görseller random sayıya göre değişir
            binding.imageOrta.setImageResource(imageArayOrta[randomOrta]) //ortadaki görseller random sayıya göre değişir

            if(kalanSure<420000){
                //eğer kalan süre 7.dakikadan az ise hızını 2 saniye yap
                handler.postDelayed(runnable,2000)
            }else{
                //eğer kalan süre 7.dakikadan az değil ise hızını 3 saniye yap
                handler.postDelayed(runnable,3000)
            }

            if(kalanSure in 240001..360001){
                //test 4.dakika ile 6.dakika arasında ise hangi resim varsa ona ait sesi çal
                //çıkan görsel araba ise araba sesi çal uçağa geçtiğinde arabayı durdur uçağı çal
                when (binding.imageOrta.drawable.constantState) {
                    ContextCompat.getDrawable(this,R.drawable.araba)?.constantState -> {
                        ses?.release()
                        ses=null
                        ses=MediaPlayer.create(this,R.raw.araba_sesi)
                        ses?.start()
                    }
                    ContextCompat.getDrawable(this,R.drawable.ucak)?.constantState -> {
                        ses?.release()
                        ses=null
                        ses=MediaPlayer.create(this,R.raw.ucak_sesi)
                        ses?.start()
                    }
                    ContextCompat.getDrawable(this,R.drawable.tavsan)?.constantState -> {
                        ses?.release()
                        ses=null
                        ses = MediaPlayer.create(this,R.raw.kus_sesi)
                        ses?.start()
                    }
                    ContextCompat.getDrawable(this,R.drawable.tilki)?.constantState -> {
                        ses?.release()
                        ses=null
                    }
                    ContextCompat.getDrawable(this,R.drawable.sincap)?.constantState -> {
                        ses?.release()
                        ses=null
                    }
                }
            }
            //yakala butonuna basıldığında ekranda tavşan görseli yoksa başarısız değişkenini arttır
            //tavşan görseli varsa başarılı değişkenini arttır
            binding.yakala.setOnClickListener {
                if(binding.imageOrta.drawable.constantState != ContextCompat.getDrawable(this, R.drawable.tavsan)?.constantState
                ){
                    basarisiz++
                    println("Başarısız tıklama: $basarisiz")

                }else{
                    basarili++
                    println("Başarılı tıklama : $basarili")
                }
            }
        }
        //tüm bunların olması için handler.post ile oluşturduğumuz runnable çalıştırırız
           handler.post(runnable)
    }
    private fun images(){
        //burada tanımladığımız listeleri doldururuz
        //üstteki alttaki soldaki sağdaki ortada çıkacak olan resimleri ekleriz
        imageArayUs.add(R.drawable.araba)
        imageArayUs.add(R.drawable.sincap)
        imageArayUs.add(R.drawable.tilki)
        imageArayUs.add(R.drawable.ucak)

        imageArayAlt.add(R.drawable.araba)
        imageArayAlt.add(R.drawable.sincap)
        imageArayAlt.add(R.drawable.ucak)
        imageArayAlt.add(R.drawable.tilki)

        imageAraySol.add(R.drawable.sincap)
        imageAraySol.add(R.drawable.araba)
        imageAraySol.add(R.drawable.ucak)
        imageAraySol.add(R.drawable.tilki)

        imageAraySag.add(R.drawable.tilki)
        imageAraySag.add(R.drawable.ucak)
        imageAraySag.add(R.drawable.sincap)
        imageAraySag.add(R.drawable.araba)

        imageArayOrta.add(R.drawable.ucak)
        imageArayOrta.add(R.drawable.tilki)
        imageArayOrta.add(R.drawable.araba)
        imageArayOrta.add(R.drawable.tavsan)
        imageArayOrta.add(R.drawable.sincap)

        imageAray.add(binding.imageUst)
        imageAray.add(binding.imageAlt)
        imageAray.add(binding.imageSol)
        imageAray.add(binding.imageSag)
        imageAray.add(binding.imageOrta)
    }

}