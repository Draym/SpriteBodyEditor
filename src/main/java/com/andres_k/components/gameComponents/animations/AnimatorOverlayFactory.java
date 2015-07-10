package com.andres_k.components.gameComponents.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by andres_k on 13/03/2015.
 */
public class AnimatorOverlayFactory extends AnimatorFactory {
    public Animator getAnimator(EnumSprites index) throws SlickException {
        if (index.getIndex() == EnumSprites.ROUND.getIndex()) {
            return this.getRoundAnimator(index);
        } else if (index.getIndex() == EnumSprites.MENU.getIndex()) {
            return this.getMenuAnimator(index);
        }
        return null;
    }

    public Animator getRoundAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.NEW_ROUND) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/newRound.png");
            animation.addFrame(img, 150);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.END_ROUND) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/victory.png");
            animation.addFrame(img, 150);
            animator.addAnimation(EnumAnimation.BASIC, animation);

            Animation animation2 = new Animation();
            Image img2 = new Image("image/overlay/defeat.png");
            animation2.addFrame(img2, 150);
            animator.addAnimation(EnumAnimation.BASIC, animation2);
        } else if (index == EnumSprites.TIMER) {
            Animation animation = new Animation();
            for (int i = 4; i > 0; --i) {
                Image img = new Image("image/overlay/roundCounter" + String.valueOf(i) + ".png");
                animation.addFrame(img, 1000);
            }
            Image img = new Image("image/overlay/roundGo.png");
            animation.addFrame(img, 1000);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }
        return animator;
    }

    public Animator getMenuAnimator(EnumSprites index) throws SlickException {
        Animator animator = new Animator();

        if (index == EnumSprites.EXIT) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/exit.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SETTINGS) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/settings.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.CONTROLS) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/controls.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SCREEN) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/screen.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.NEW) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/new.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.GO) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/go.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        } else if (index == EnumSprites.SAVE) {
            Animation animation = new Animation();
            Image img = new Image("image/overlay/save.png");
            animation.addFrame(img, 150);
            animation.setLooping(false);
            animator.addAnimation(EnumAnimation.BASIC, animation);
        }

        return animator;
    }
}
