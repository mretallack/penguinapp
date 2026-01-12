package com.penguin.care

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var hunger = 50
    private var happiness = 50
    private var energy = 50
    private lateinit var penguinImage: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var isPerformingAction = false
    
    // New movement and animation variables
    private var penguinX = 0f
    private var penguinY = 0f
    private var movingRight = true
    private var currentIdleAction = "idle"
    private val random = kotlin.random.Random
    
    companion object {
        private const val TAG = "PenguinCare"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate started")
        setContentView(R.layout.activity_main)

        penguinImage = findViewById(R.id.penguinImage)
        val feedButton = findViewById<Button>(R.id.feedButton)
        val playButton = findViewById<Button>(R.id.playButton)
        val sleepButton = findViewById<Button>(R.id.sleepButton)
        val testButton = findViewById<Button>(R.id.testButton)

        testButton.setOnClickListener {
            startActivity(Intent(this, SpriteTestActivity::class.java))
        }

        feedButton.setOnClickListener {
            Log.d(TAG, "Feed button clicked")
            isPerformingAction = true
            currentIdleAction = "feeding"
            
            // Enhanced feeding interaction
            hunger = minOf(100, hunger + 25)
            happiness = minOf(100, happiness + 10) // Feeding makes penguin happy too
            
            // Show eating animation sequence
            animatePenguin("walk") // Waddle to food
            handler.postDelayed({
                animatePenguin("slide") // Lean down to eat
                handler.postDelayed({
                    animatePenguin("bounce") // Happy bounce after eating
                    handler.postDelayed({
                        isPerformingAction = false
                        currentIdleAction = "idle"
                        animatePenguin("idle")
                    }, 1000L)
                }, 1500L)
            }, 1000L)
            
            updateStats()
        }

        playButton.setOnClickListener {
            Log.d(TAG, "Play button clicked")
            isPerformingAction = true
            currentIdleAction = "playing"
            
            // Enhanced play interaction
            happiness = minOf(100, happiness + 30)
            energy = maxOf(0, energy - 15)
            
            // Show playful animation sequence
            animatePenguin("bounce") // Jump excitedly
            handler.postDelayed({
                startSlidingAnimation() // Slide around playfully
                handler.postDelayed({
                    animatePenguin("bounce") // Another happy bounce
                    handler.postDelayed({
                        animatePenguin("walk") // Walk around happily
                        handler.postDelayed({
                            isPerformingAction = false
                            currentIdleAction = "idle"
                            animatePenguin("idle")
                        }, 1500L)
                    }, 800L)
                }, 1500L)
            }, 800L)
            
            updateStats()
        }

        sleepButton.setOnClickListener {
            Log.d(TAG, "Sleep button clicked")
            isPerformingAction = true
            currentIdleAction = "sleeping"
            
            // Enhanced sleep interaction
            energy = minOf(100, energy + 35)
            happiness = minOf(100, happiness + 5) // Rest makes penguin slightly happier
            
            // Show sitting/sleeping sequence
            penguinImage.setImageResource(R.drawable.penguin_sitting_anim)
            val animation = penguinImage.drawable as AnimationDrawable
            animation.start()
            
            // After sitting animation completes (about 13.8 seconds total)
            handler.postDelayed({
                isPerformingAction = false
                currentIdleAction = "idle"
                animatePenguin("idle")
            }, 14000L)
            
            updateStats()
        }

        updateStats()
        
        // Start animations after view is ready
        penguinImage.post {
            Log.d(TAG, "Starting idle animation from onCreate")
            startIdleAnimation()
        }
        
        startStatDecay()
        startRandomBlinks()
        startIdleBehavior() // Add new idle behavior system
        Log.d(TAG, "onCreate completed")
    }

    private fun animatePenguin(state: String) {
        Log.d(TAG, "animatePenguin called with state: $state")
        isPerformingAction = state != "idle" && state != "sad"
        
        val animationDrawable = when (state) {
            "walk" -> R.drawable.penguin_walk_anim
            "slide" -> R.drawable.penguin_slide_anim
            "bounce" -> R.drawable.penguin_bounce_anim
            "unhappy" -> R.drawable.penguin_unhappy_anim
            "happy" -> R.drawable.penguin_walk_anim
            "play" -> R.drawable.penguin_run_anim
            "sleep" -> R.drawable.penguin_slide_anim
            "sad" -> R.drawable.penguin_hurt_anim
            "blink" -> R.drawable.penguin_blink_anim
            else -> R.drawable.penguin_idle_anim
        }
        
        Log.d(TAG, "Setting animation resource: $animationDrawable")
        penguinImage.setImageResource(animationDrawable)
        val animation = penguinImage.drawable as AnimationDrawable
        animation.start()
        Log.d(TAG, "Animation started for state $state, isRunning: ${animation.isRunning}")
        
        if (state == "blink") {
            handler.postDelayed({ 
                Log.d(TAG, "Blink animation finished, returning to idle")
                isPerformingAction = false
                startIdleAnimation()
            }, 500)
        } else if (state != "idle" && state != "sad") {
            handler.postDelayed({ 
                Log.d(TAG, "Action animation finished, returning to idle")
                isPerformingAction = false
                startIdleAnimation()
            }, 2000)
        }
    }

    private fun startIdleAnimation() {
        Log.d(TAG, "startIdleAnimation called")
        val currentState = when {
            hunger < 30 || happiness < 30 || energy < 20 -> "sad"
            else -> "idle"
        }
        
        Log.d(TAG, "Current state: $currentState (hunger=$hunger, happiness=$happiness, energy=$energy)")
        
        penguinImage.setImageResource(
            if (currentState == "sad") R.drawable.penguin_hurt_anim 
            else R.drawable.penguin_idle_anim
        )
        
        Log.d(TAG, "Image resource set, starting animation")
        
        // Ensure animation starts after drawable is set
        handler.post {
            val animation = penguinImage.drawable as? AnimationDrawable
            if (animation != null) {
                Log.d(TAG, "Animation drawable found, starting animation")
                animation.start()
                Log.d(TAG, "Animation started, isRunning: ${animation.isRunning}")
            } else {
                Log.e(TAG, "Animation drawable is null!")
            }
        }
    }

    private fun startStatDecay() {
        handler.postDelayed({
            hunger = maxOf(0, hunger - 1)
            happiness = maxOf(0, happiness - 1)
            energy = maxOf(0, energy - 1)
            updateStats()
            
            // Check if we need to change animation state
            val newState = when {
                hunger < 30 || happiness < 30 || energy < 20 -> "sad"
                else -> "idle"
            }
            
            // Only change if current animation is idle or sad (not during actions)
            val currentDrawable = penguinImage.drawable
            if (currentDrawable == getDrawable(R.drawable.penguin_idle_anim) || 
                currentDrawable == getDrawable(R.drawable.penguin_hurt_anim)) {
                startIdleAnimation()
            }
            
            startStatDecay()
        }, 3000)
    }

    private fun startRandomBlinks() {
        val randomDelay = (3000..8000).random() // Random delay between 3-8 seconds
        handler.postDelayed({
            // Only blink if not performing an action and in idle/sad state
            if (!isPerformingAction) {
                val currentState = when {
                    hunger < 30 || happiness < 30 || energy < 20 -> "sad"
                    else -> "idle"
                }
                
                if (currentState == "idle") {
                    animatePenguin("blink")
                }
            }
            startRandomBlinks() // Schedule next blink
        }, randomDelay.toLong())
    }

    private fun updateStats() {
        findViewById<ProgressBar>(R.id.hungerBar).progress = hunger
        findViewById<ProgressBar>(R.id.happinessBar).progress = happiness
        findViewById<ProgressBar>(R.id.energyBar).progress = energy
    }
    
    private fun startIdleBehavior() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isPerformingAction) {
                    performRandomIdleAction()
                }
                // Schedule next idle action based on happiness (happier = more active)
                val delay = when {
                    happiness > 70 -> 2000L + random.nextLong(1000L) // Very active
                    happiness > 40 -> 3000L + random.nextLong(2000L) // Moderately active  
                    else -> 5000L + random.nextLong(3000L) // Less active when unhappy
                }
                handler.postDelayed(this, delay)
            }
        })
    }
    
    private fun performRandomIdleAction() {
        val actions = when {
            happiness > 70 -> listOf("walk", "bounce", "slide", "idle") // Happy penguin
            happiness > 40 -> listOf("walk", "walk", "idle", "bounce") // Normal penguin
            else -> listOf("idle", "idle", "unhappy", "walk") // Unhappy penguin
        }
        
        val action = actions.random()
        Log.d(TAG, "Performing idle action: $action (happiness: $happiness)")
        
        when (action) {
            "walk" -> startWalkingAnimation()
            "slide" -> startSlidingAnimation() 
            "bounce" -> startBouncingAnimation()
            "unhappy" -> if (happiness < 50) startUnhappyAnimation()
            else -> animatePenguin("idle")
        }
    }
    
    private fun startWalkingAnimation() {
        currentIdleAction = "walking"
        
        // Determine direction and distance
        val screenWidth = resources.displayMetrics.widthPixels
        val maxDistance = screenWidth / 4
        val distance = random.nextInt(100, maxDistance)
        
        // Flip direction randomly or when hitting edges
        if (penguinImage.x <= 0) movingRight = true
        else if (penguinImage.x >= screenWidth - 200) movingRight = false
        else movingRight = random.nextBoolean()
        
        animatePenguin("walk")
        
        // Move penguin with waddle effect
        val startX = penguinImage.x
        val endX = if (movingRight) startX + distance else startX - distance
        
        penguinImage.animate()
            .x(endX.coerceIn(0f, (screenWidth - 200).toFloat()))
            .setDuration(2000L)
            .withEndAction {
                if (currentIdleAction == "walking") {
                    animatePenguin("idle")
                    currentIdleAction = "idle"
                }
            }
        
        // Flip sprite for left movement
        penguinImage.scaleX = if (movingRight) 1f else -1f
    }
    
    private fun startSlidingAnimation() {
        currentIdleAction = "sliding"
        animatePenguin("slide")
        
        val screenWidth = resources.displayMetrics.widthPixels
        val startX = penguinImage.x
        val slideDistance = screenWidth / 3
        val endX = if (movingRight) startX + slideDistance else startX - slideDistance
        
        penguinImage.animate()
            .x(endX.coerceIn(0f, (screenWidth - 200).toFloat()))
            .setDuration(1500L)
            .withEndAction {
                if (currentIdleAction == "sliding") {
                    animatePenguin("idle")
                    currentIdleAction = "idle"
                }
            }
        
        penguinImage.scaleX = if (movingRight) 1f else -1f
        movingRight = !movingRight // Change direction after slide
    }
    
    private fun startBouncingAnimation() {
        currentIdleAction = "bouncing"
        animatePenguin("bounce")
        
        // Bounce in place with slight Y movement
        penguinImage.animate()
            .translationY(-30f)
            .setDuration(200L)
            .withEndAction {
                penguinImage.animate()
                    .translationY(0f)
                    .setDuration(200L)
                    .withEndAction {
                        if (currentIdleAction == "bouncing") {
                            animatePenguin("idle")
                            currentIdleAction = "idle"
                        }
                    }
            }
    }
    
    private fun startUnhappyAnimation() {
        currentIdleAction = "unhappy"
        animatePenguin("unhappy")
        
        handler.postDelayed({
            if (currentIdleAction == "unhappy") {
                animatePenguin("idle")
                currentIdleAction = "idle"
            }
        }, 2000L)
    }
}
