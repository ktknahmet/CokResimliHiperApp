package com.example.uygulama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.uygulama.databinding.ActivityMainBinding
import com.example.uygulama.databinding.ActivityRaporBinding

class RaporActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRaporBinding
    private var gelenBasarisiz:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaporBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = intent
        gelenBasarisiz=intent.getIntExtra("Basarisiz",0)
        println("Gelen Başarısız : $gelenBasarisiz")

        raporlar()
    }

    private fun raporlar(){
        if(gelenBasarisiz!! <10 ){
            binding.dikkatPerformansDetay.setText(R.string.dikkat_performans_detay_normal)
            binding.zamanlamaPerformansDetay.setText(R.string.zamanlama_performans_detay_normal)
            binding.durustsellikDetay.setText(R.string.durustsellik_detay_normal)
            binding.hiperAktifDetay.setText(R.string.hiperaktif_detay_normal)

            binding.tavsiye.visibility= View.INVISIBLE
            binding.tavsiyeDetay.visibility= View.INVISIBLE
            binding.ogretmen.visibility= View.INVISIBLE
            binding.ogretmenDetay.visibility= View.INVISIBLE
            binding.aile.visibility= View.INVISIBLE
            binding.aileDetay.visibility= View.INVISIBLE
        }
        if(gelenBasarisiz!! in 11..15){
            binding.dikkatPerformansDetay.setText(R.string.dikkat_performans_detay_orta)
            binding.zamanlamaPerformansDetay.setText(R.string.zamanlama_performans_detay_orta)
            binding.durustsellikDetay.setText(R.string.durustsellik_detay_orta)
            binding.hiperAktifDetay.setText(R.string.hiperaktif_detay_orta)

            binding.tavsiyeDetay.setText(R.string.tavsiye_detay_orta)
            binding.ogretmenDetay.setText(R.string.ogretmen_detay_orta)
            binding.aileDetay.setText(R.string.ailedetay_orta)
        }
    }
}