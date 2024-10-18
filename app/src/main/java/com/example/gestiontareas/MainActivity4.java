package com.example.gestiontareas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity4 extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Referencia a ViewPager2 y TabLayout desde el XML
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Crear el adaptador para ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Conectar el TabLayout con ViewPager2 mediante TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Títulos de las pestañas
                    switch (position) {
                        case 0:
                            tab.setText("Grupos Creados");
                            break;
                        case 1:
                            tab.setText("Grupos Unidos");
                            break;
                    }
                }).attach();
    }
}
