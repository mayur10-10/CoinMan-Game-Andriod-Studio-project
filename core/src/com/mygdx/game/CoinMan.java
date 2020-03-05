package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manState=0;
	int pause=0;
	int manY=0;
	float gravity=0.4f;
	float velocity=0;
	Rectangle manRectangle;
	int score=0;
	BitmapFont bitmapFont;
	int gamestate=0;

	ArrayList<Integer> coinXs= new ArrayList<Integer>();
	ArrayList<Integer> coinYs= new ArrayList<Integer>();
	ArrayList<Integer> bombXs= new ArrayList<Integer>();
	ArrayList<Integer> bombYs= new ArrayList<Integer>();
	ArrayList<Rectangle> coinRec=new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRec=new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;
	int bombcount;
	Texture bomb;
	Random random;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		coin=new Texture("coin.png");
		dizzy=new Texture("dizzy-1.png");
		bomb=new Texture("bomb.png");
		man= new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().setScale(10);
	}

	public void makeCoin(){
		random=new Random();
		float height=random.nextFloat()* Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makebomb(){
		random=new Random();
		float height=random.nextFloat()* Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gamestate==1){
			if (coinCount<100){
				coinCount++;
			}
			else {
				coinCount=0;
				makeCoin();
			}

			coinRec.clear();
			for (int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i) -4);
				coinRec.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}

			if (bombcount<250){
				bombcount++;
			}
			else {
				bombcount=0;
				makebomb();
			}

			bombRec.clear();
			for (int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i) -4);
				bombRec.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity= -10;
			}

			if (pause<6){
				pause++;
			}
			else {
				pause=0;
				if (manState < 2) {
					manState++;
				} else {
					manState = 0;
				}
			}

			velocity+=gravity;
			manY -= velocity;

			if (manY <=0){
				manY=0;
			}
		}
		else if(gamestate==0){
			// waiting to start
			if (Gdx.input.justTouched()){
				gamestate=1;
			}
		}
		else if (gamestate==2){
			if (Gdx.input.justTouched()) {
				gamestate=1;
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinXs.clear();
				coinYs.clear();
				coinRec.clear();
				coinCount=0;
				bombRec.clear();
				bombXs.clear();
				bombYs.clear();
				bombcount=0;
			}
		}

		if (gamestate==2) {
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		else {
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}

		manRectangle=new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());

		for (int i=0; i< coinRec.size();i++){
			if (Intersector.overlaps(manRectangle,coinRec.get(i))){
				score++;
				coinRec.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i=0; i< bombRec.size();i++){
			if (Intersector.overlaps(manRectangle,bombRec.get(i))){
				gamestate=2;
			}
		}

		bitmapFont.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
