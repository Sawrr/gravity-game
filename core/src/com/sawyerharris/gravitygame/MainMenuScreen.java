package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * MainMenu
 * 
 * @author Sawyer Harris
 *
 */
public class MainMenuScreen implements Screen {

	private GravityGame game;
	private Stage stage;
	private FillViewport viewport;
	private Skin skin;
	
	private float screenWidth;
	private float screenHeight;
	
    private static final float BUTTON_MARGIN = 50f;
    private static final float BUTTON_VERTICAL_MARGIN = 300f;
    private static final float BUTTON_HEIGHT = 120f;
    private static final float BUTTON_SPACING = 20f;
    
	public MainMenuScreen(GravityGame gam) {
		game = gam;
		skin = GravityGame.getSkin();
		viewport = new FillViewport(GravityGame.getScreenWidth(), GravityGame.getScreenHeight());
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		
		screenWidth = GravityGame.getScreenWidth();
		screenHeight = GravityGame.getScreenHeight();
		
		createButtons();
	}
	
	public void createButtons() {
		final float buttonWidth = screenWidth - 2 * BUTTON_MARGIN;
		final float buttonX = BUTTON_MARGIN;
        float currentY = screenHeight - BUTTON_VERTICAL_MARGIN;
 
        Label titleLabel = new Label("Gravity Game", skin);
        titleLabel.setX(BUTTON_MARGIN);
        titleLabel.setY(currentY);
        titleLabel.setWidth(buttonWidth);
        titleLabel.setHeight(BUTTON_HEIGHT);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);
        stage.addActor(titleLabel);
 
        TextButton playGameButton = new TextButton( "Play", skin );
        playGameButton.setX(BUTTON_MARGIN);
        playGameButton.setY(currentY);
        playGameButton.setWidth(buttonWidth);
        playGameButton.setHeight(BUTTON_HEIGHT);
        playGameButton.getLabel().setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);         
        playGameButton.addListener(new ActorGestureListener() {
        	@Override
        	public void tap(InputEvent event, float x, float y, int pointer, int button) {
        		int current = GravityGame.getCurrentLevel();
        		Level level = GravityGame.getLevels().get(GravityGame.getLevelNames().get(current));
        		game.playLevel(level, false);
        	}
        });
        stage.addActor(playGameButton);
        
        TextButton levelSelectButton = new TextButton( "Level Select", skin );
        levelSelectButton.setX(BUTTON_MARGIN);
        levelSelectButton.setY(currentY);
        levelSelectButton.setWidth(buttonWidth);
        levelSelectButton.setHeight(BUTTON_HEIGHT);
        levelSelectButton.getLabel().setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);         
        levelSelectButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		game.setScreen(new LevelSelectScreen(game));
        		return true;
        	}
        });
        stage.addActor(levelSelectButton);
        
        TextButton levelEditorButton = new TextButton( "Level Editor", skin );
        levelEditorButton.setX(BUTTON_MARGIN);
        levelEditorButton.setY(currentY);
        levelEditorButton.setWidth(buttonWidth);
        levelEditorButton.setHeight(BUTTON_HEIGHT);
        levelEditorButton.getLabel().setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);         
        levelEditorButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		// TODO level editor screen
        		return true;
        	}
        });
        stage.addActor(levelEditorButton);
        
        TextButton tutorialButton = new TextButton( "Tutorial", skin );
        tutorialButton.setX(BUTTON_MARGIN);
        tutorialButton.setY(currentY);
        tutorialButton.setWidth(buttonWidth);
        tutorialButton.setHeight(BUTTON_HEIGHT);
        tutorialButton.getLabel().setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);         
        tutorialButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		Level level = GravityGame.getTutorialLevels().get(GravityGame.getTutorialLevelNames().get(0));
        		game.playLevel(level, false);
        		return true;
        	}
        });
        stage.addActor(tutorialButton);
        
        TextButton optionsButton = new TextButton( "Options", skin );
        optionsButton.setX(BUTTON_MARGIN);
        optionsButton.setY(currentY);
        optionsButton.setWidth(buttonWidth);
        optionsButton.setHeight(BUTTON_HEIGHT);
        optionsButton.getLabel().setFontScale(3f);
        currentY -= (BUTTON_HEIGHT + BUTTON_SPACING);         
        optionsButton.addListener(new ClickListener() {
        	@Override
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        		// TODO Options screen
        		return true;
        	}
        });
        stage.addActor(optionsButton);
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
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			viewport.update(width, height);
		}
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
		stage.dispose();
	}
}
