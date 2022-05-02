package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class MovingTexture extends Texture {
    private float speedX = 0, speedY = 0;
    private float x = 0, y = 0;
    private float width, height;
    private boolean movEnabled = true;

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public MovingTexture(FileHandle internalPath){
        super(internalPath);
    }

    public MovingTexture(String internalPath){
        super(internalPath);
    }

    public MovingTexture(FileHandle internalPath, float speedX, float speedY){
        super(internalPath);
        this.speedX = speedX;
        this.speedY = speedY;
        this.width = this.getWidth();
        this.height = this.getHeight();
    }

    public MovingTexture(String internalPath, float x, float y, float speedX, float speedY){
        super(internalPath);
        this.speedX = speedX;
        this.speedY = speedY;
        this.x = x;
        this.y = y;
        this.width = this.getWidth();
        this.height = this.getHeight();
    }

    public MovingTexture(FileHandle internalPath, float x, float y, float speedX, float speedY){
        super(internalPath);
        this.speedX = speedX;
        this.speedY = speedY;
        this.x = x;
        this.y = y;
        this.width = this.getWidth();
        this.height = this.getHeight();
    }

    public MovingTexture(String internalPath, float speedX, float speedY){
        super(internalPath);
        this.speedX = speedX;
        this.speedY = speedY;
        this.width = this.getWidth();
        this.height = this.getHeight();
    }

    private void updateSpeed(float viewWidth, float viewHeight, float margin){
        if(x + width  < viewWidth +margin || x > 0 - margin)
            speedX *= -1;

        if(y + height < viewHeight +margin || y > 0 - margin)
            speedY *= -1;
    }

    public void updatePosition(float viewWidth, float viewHeight, float margin){
        if(movEnabled){
            updateSpeed(viewWidth, viewHeight, margin);
            x += Gdx.graphics.getDeltaTime() * speedX;
            y += Gdx.graphics.getDeltaTime() * speedY;
        }
    }

    public void stop(){
        movEnabled = false;
    }

    public void resume(){
        movEnabled = true;
    }
}
