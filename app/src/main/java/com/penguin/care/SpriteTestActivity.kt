package com.penguin.care

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class SpriteTestActivity : Activity() {
    private lateinit var imageView: ImageView
    private lateinit var statusText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }
        
        statusText = TextView(this).apply {
            text = "Penguin Sprite Test - All 256 Sprites"
            textSize = 16f
        }
        
        imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(200, 200)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        
        layout.addView(statusText)
        layout.addView(imageView)
        
        // Organize sprites by category
        val spriteCategories = listOf(
            "Walking" to (0..8).map { "penguin_walk_${String.format("%02d", it)}" },
            "Jumping" to (0..6).map { "penguin_jump_${String.format("%02d", it)}" },
            "Sliding" to (0..15).map { "penguin_slide_${String.format("%02d", it)}" },
            "Bouncing" to (0..15).map { "penguin_bounce_${String.format("%02d", it)}" },
            "Actions" to (0..15).map { "penguin_action_${String.format("%02d", it)}" },
            "Misc" to (0..15).map { "penguin_misc_${String.format("%02d", it)}" },
            "Sleeping" to (0..15).map { "penguin_sleep_${String.format("%02d", it)}" },
            "Resting" to (0..15).map { "penguin_rest_${String.format("%02d", it)}" },
            "States" to (0..15).map { "penguin_state_${String.format("%02d", it)}" },
            "Emotions" to (0..15).map { "penguin_emotion_${String.format("%02d", it)}" },
            "Special" to (0..15).map { "penguin_special_${String.format("%02d", it)}" },
            "Extra" to (0..15).map { "penguin_extra_${String.format("%02d", it)}" },
            "Ill/Hurt" to (0..15).map { "penguin_ill_${String.format("%02d", it)}" },
        )
        
        spriteCategories.forEach { (category, sprites) ->
            // Category header
            val header = TextView(this).apply {
                text = category
                textSize = 18f
                setPadding(0, 20, 0, 10)
            }
            layout.addView(header)
            
            // Create horizontal layout for category buttons
            val buttonLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            
            sprites.take(8).forEach { spriteName ->
                val button = Button(this).apply {
                    text = spriteName.split("_").last()
                    setOnClickListener { 
                        testSprite(spriteName, "$category: $spriteName")
                    }
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }
                buttonLayout.addView(button)
            }
            layout.addView(buttonLayout)
            
            // Second row if more than 8 sprites
            if (sprites.size > 8) {
                val buttonLayout2 = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                
                sprites.drop(8).forEach { spriteName ->
                    val button = Button(this).apply {
                        text = spriteName.split("_").last()
                        setOnClickListener { 
                            testSprite(spriteName, "$category: $spriteName")
                        }
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    }
                    buttonLayout2.addView(button)
                }
                layout.addView(buttonLayout2)
            }
        }
        
        scrollView.addView(layout)
        setContentView(scrollView)
        
        // Test first walking sprite
        testSprite("penguin_walk_00", "Walking: penguin_walk_00")
    }
    
    private fun testSprite(spriteName: String, label: String) {
        try {
            val resourceId = resources.getIdentifier(spriteName, "drawable", packageName)
            if (resourceId != 0) {
                imageView.setImageResource(resourceId)
                statusText.text = "✓ $label loaded successfully"
            } else {
                statusText.text = "✗ $label not found in resources"
            }
        } catch (e: Exception) {
            statusText.text = "✗ Error loading $label: ${e.message}"
        }
    }
}
