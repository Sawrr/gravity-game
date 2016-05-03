package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Level Select
 * 
 * @author Sawyer Harris
 *
 */
public class LevelSelectScreen implements Screen {

	private GravityGame game;
	private Stage stage;
	private FillViewport viewport;
	private Skin skin;
	
	private float screenWidth;
	private float screenHeight;
	
    private static final float MARGIN = 50f;
    private static final float BUTTON_VERTICAL_MARGIN = 300f;
    private static final float BUTTON_HEIGHT = 120f;
    private static final float BUTTON_SPACING = 20f;
    private static final float SCROLL_PANE_HEIGHT = 900f;
    
	public LevelSelectScreen(GravityGame gam) {
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
		final float buttonWidth = screenWidth - 2 * MARGIN;
		final float buttonX = MARGIN;
        float currentY = screenHeight - BUTTON_VERTICAL_MARGIN;
 
        Label titleLabel = new Label("Level Select", skin);
        titleLabel.setX(MARGIN);
        titleLabel.setY(currentY);
        titleLabel.setWidth(buttonWidth);
        titleLabel.setHeight(BUTTON_HEIGHT);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(3f);
        currentY -= (SCROLL_PANE_HEIGHT + BUTTON_SPACING);
        stage.addActor(titleLabel);
             
        // TODO add label and button to swap to custom levels
        
        Table table = new Table();
        for (final String levelName : GravityGame.getLevelNames()) {
        	final Level level = GravityGame.getLevels().get(levelName);
        	final int id = GravityGame.getLevelNames().indexOf(levelName);
        	TextButton button = new TextButton(level.getName(), skin);
        	button.setWidth(buttonWidth);
            button.setHeight(BUTTON_HEIGHT);
            button.getLabel().setFontScale(3f);
            if (id <= GravityGame.getHighestLevel()) {
            	button.addListener(new ActorGestureListener() {
                	@Override
                	public void tap(InputEvent event, float x, float y, int pointer, int button) {
                		GravityGame.setCurrentLevel(id);
                		game.playLevel(level, false);
                	}
                });
            } else {
            	button.setDisabled(true);
            	button.setText("?");
            }
            
        	table.add(button).minWidth(buttonWidth).minHeight(BUTTON_HEIGHT);
        	table.row();
        }
        
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setX(MARGIN);
        scrollPane.setY(currentY);
        scrollPane.setWidth(buttonWidth);
        scrollPane.setHeight(SCROLL_PANE_HEIGHT);
        scrollPane.setScrollingDisabled(true, false);
        stage.addActor(scrollPane);
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