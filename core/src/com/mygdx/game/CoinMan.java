package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,coin,bomb;
	Texture []man;
	int manstate=0,pause=0;
	ArrayList<Float> CoinX,CoinY,BombX,BombY;
	ArrayList<Rectangle> Coinrect,Bombrect;
	Rectangle manrect;
	int bombcount=0,score=0;
	int coincount=0,gamestate=0;
	BitmapFont scorefont;
	Texture dizzy;
	float gravity=0.2f,velocity=0,manY=0,jumper=0;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
	    man=new Texture[4];
	    man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-3.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		CoinX=new ArrayList<>();
		CoinY=new ArrayList<>();
		BombX=new ArrayList<>();
		BombY=new ArrayList<>();
		Coinrect=new ArrayList<>();
		Bombrect=new ArrayList<>();
		manrect=new Rectangle();
		scorefont=new BitmapFont();
		scorefont.setColor(Color.WHITE);
		scorefont.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");
	}

	@Override
	public void render () {
		batch.begin();
		Coinrect.clear();
		Bombrect.clear();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate==1)
        {
            if(coincount<100)
                coincount++;
            else
            {
                coincount=0;
                makecoin();
            }
            if(bombcount<250)
                bombcount++;
            else
            {
                bombcount=0;
                makebomb();
            }
            if(Gdx.input.justTouched())
            {
                velocity=-10;
            }

            if(pause<3)
                pause++;
            else
            {
                pause=0;
                if(manstate<3)
                    manstate++;
                else
                    manstate=0;

            }

            if(manY<=0)
                manY=0;
            if(manY>=Gdx.graphics.getHeight())
                manY=Gdx.graphics.getHeight();

                velocity += gravity;
                manY -= velocity;

            for(int i=0;i<CoinX.size();i++)
            {
                batch.draw(coin,CoinX.get(i),CoinY.get(i));
                CoinX.set(i,CoinX.get(i)-4);
                Coinrect.add(new Rectangle(CoinX.get(i),CoinY.get(i),coin.getWidth(),coin.getHeight()));
            }
            for(int i=0;i<BombX.size();i++)
            {
                batch.draw(bomb,BombX.get(i),BombY.get(i));
                BombX.set(i,BombX.get(i)-8);
                Bombrect.add(new Rectangle(BombX.get(i),BombY.get(i),bomb.getWidth(),bomb.getHeight()));
            }
            for(int i=0;i<Coinrect.size();i++)
            {
                if(Intersector.overlaps(Coinrect.get(i),manrect))
                {
                    score++;
                    Coinrect.remove(i);
                    CoinX.remove(i);
                    CoinY.remove(i);
                    break;
                    //Gdx.app.log("Coin","Collision");
                }
            }
            for(int i=0;i<Bombrect.size();i++)
            {
                if(Intersector.overlaps(Bombrect.get(i),manrect))
                {

                    gamestate=2;
                    //Gdx.app.log("Bomb","Collision");
                }
            }

        }
		else if(gamestate==0)
        {
        if(Gdx.input.justTouched())
            gamestate=1;
        }
		else if(gamestate==2)
        {
            if(Gdx.input.justTouched())
            {
                gamestate = 1;
                manY=Gdx.graphics.getHeight()/2;
                velocity = 0;
                score = 0;
                Coinrect.clear();
                coincount = 0;
                CoinX.clear();
                CoinY.clear();
                Bombrect.clear();
                BombY.clear();
                BombX.clear();
            }
        }
        if(gamestate==2)
            batch.draw(dizzy,Gdx.graphics.getWidth()/2-dizzy.getWidth()/2,manY);
        else
        batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);


		manrect=new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
		scorefont.draw(batch,String.valueOf(score),100,200);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
	public void makecoin()
	{
		float height=new Random().nextFloat()*Gdx.graphics.getHeight();
		CoinX.add((float)Gdx.graphics.getWidth());
		CoinY.add(height);
	}
	public void makebomb()
	{
		float height=new Random().nextFloat()*Gdx.graphics.getHeight();
		BombX.add((float)Gdx.graphics.getWidth());
		BombY.add(height);

	}


}
