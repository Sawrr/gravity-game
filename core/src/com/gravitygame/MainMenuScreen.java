package com.gravitygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {
	final GravityGame game;
	int screenWidth;
	int screenHeight;
	OrthographicCamera camera;

	public MainMenuScreen(final GravityGame game, int width, int height) {
		this.game = game;
		screenWidth = width;
		screenHeight = height;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Gravity Game", screenWidth/2, screenHeight/2);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game, "testmap"));
			dispose();
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}