package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * MainMenu
 * Currently using borrowed code
 * 
 * @author Sawyer Harris
 *
 */
public class MainMenuScreen implements Screen {

	private Stage stage;
	private Skin skin;
	
    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float BUTTON_SPACING = 10f;
    
	public MainMenuScreen() {
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
        final float buttonX = ( width - BUTTON_WIDTH ) / 2;
        float currentY = 500f;
 
        // label "welcome"
        Label welcomeLabel = new Label( "Gravity Game", skin );
        welcomeLabel.setX(( ( width - welcomeLabel.getWidth() ) / 2 ));
        welcomeLabel.setY(( currentY + 100 ));
        stage.addActor( welcomeLabel );
 
        // button "start game"
        TextButton startGameButton = new TextButton( "Start game", skin );
        startGameButton.setX(buttonX);
        startGameButton.setY(currentY);
        startGameButton.setWidth(BUTTON_WIDTH);
        startGameButton.setHeight(BUTTON_HEIGHT);
        stage.addActor( startGameButton );
 
        // button "options"
        TextButton optionsButton = new TextButton( "Options", skin );
        optionsButton.setX(buttonX);
        optionsButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        optionsButton.setWidth(BUTTON_WIDTH);
        optionsButton.setHeight(BUTTON_HEIGHT);
        stage.addActor( optionsButton );
 
        // button "hall of fame"
        TextButton hallOfFameButton = new TextButton( "Hall of Fame", skin );
        hallOfFameButton.setX(buttonX);
        hallOfFameButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        hallOfFameButton.setWidth(BUTTON_WIDTH);
        hallOfFameButton.setHeight(BUTTON_HEIGHT);
        stage.addActor( hallOfFameButton );
        
        startGameButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		System.out.println("pressed");
        		return true;
        	}
        });
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
