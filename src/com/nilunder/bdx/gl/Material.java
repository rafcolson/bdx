package com.nilunder.bdx.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nilunder.bdx.Scene;
import com.nilunder.bdx.utils.Color;
import com.nilunder.bdx.utils.Named;

public class Material extends com.badlogic.gdx.graphics.g3d.Material implements Named, Disposable {

	public Texture currentTexture;
	public Shader shader;
	public String texturePath;

	public Material() {
		super();
	}

	public Material(String id) {
		super(id);
	}

	public Material(Attribute... attributes) {
		super(attributes);
	}

	public Material(String id, Attribute... attributes) {
		super(id, attributes);
	}

	public Material(Array<Attribute> attributes) {
		super(attributes);
	}

	public Material(String id, Array<Attribute> attributes) {
		super(id, attributes);
	}

	public Material(Material copyFrom) {
		super(copyFrom);
		shader = copyFrom.shader;
		currentTexture = copyFrom.currentTexture;
	}

	public Material(com.badlogic.gdx.graphics.g3d.Material copyFrom){
		super(copyFrom);
	}

	public Material(String id, Material copyFrom) {
		super(id, copyFrom);
	}

	public Color color(){
		ColorAttribute ca = (ColorAttribute) get(ColorAttribute.Diffuse);
		return new Color(ca.color);
	}

	public void color(Color color){
		ColorAttribute ca = (ColorAttribute) get(ColorAttribute.Diffuse);
		ca.color.set(color);
		if (get(BlendingAttribute.Type) != null) {
			BlendingAttribute ba = (BlendingAttribute) get(BlendingAttribute.Type);
			ba.opacity = color.a;
		}
	}

	public Color tint(){
		ColorAttribute ta = (ColorAttribute) get(Scene.BDXColorAttribute.Tint);
		return new Color(ta.color);
	}

	public void tint(Color color){
		ColorAttribute ta = (ColorAttribute) get(Scene.BDXColorAttribute.Tint);
		ta.color.set(color);
	}

	public int[] blendMode(){
		BlendingAttribute ba = (BlendingAttribute) get(BlendingAttribute.Type);
		return new int[]{ba.sourceFunction, ba.destFunction};
	}

	public void blendMode(int src, int dest){
		BlendingAttribute ba = (BlendingAttribute) get(BlendingAttribute.Type);
		ba.sourceFunction = src;
		ba.destFunction = dest;
	}

	public void backToFrontSorting(boolean on) {
		BlendingAttribute ba = (BlendingAttribute) get(BlendingAttribute.Type);
		ba.blended = on;		// Has to be true for the blend mode to take effect
	}

	public boolean backToFrontSorting() {
		BlendingAttribute ba = (BlendingAttribute) get(BlendingAttribute.Type);
		return ba.blended;
	}

	public void depthMask(boolean on) {
		DepthTestAttribute da = (DepthTestAttribute) get(DepthTestAttribute.Type);
		da.depthMask = on;
	}

	public boolean depthMask() {
		DepthTestAttribute da = (DepthTestAttribute) get(DepthTestAttribute.Type);
		return da.depthMask;
	}

	public void depthFunction(int depthFunc) {
		DepthTestAttribute da = (DepthTestAttribute) get(DepthTestAttribute.Type);
		da.depthFunc = depthFunc;
	}

	public int depthFunction() {
		DepthTestAttribute da = (DepthTestAttribute) get(DepthTestAttribute.Type);
		return da.depthFunc;
	}

	public boolean shadeless(){
		IntAttribute sa = (IntAttribute) get(Scene.BDXIntAttribute.Shadeless);
		return sa.value == 1;
	}

	public void shadeless(boolean shadeless){
		IntAttribute sa = (IntAttribute) get(Scene.BDXIntAttribute.Shadeless);
		sa.value = shadeless ? 1 : 0;
	}

	public String name(){
		return id;
	}

	public String toString(){
		return id + " <" + getClass().getName() + "> @" + Integer.toHexString(hashCode());
	}

	public Texture texture(String filename) {
		Texture tex = currentTexture;
		if (!filename.equals(texturePath)) {
			tex = new Texture(Gdx.files.internal("bdx/textures/" + filename));
			tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
			texture(tex);
			texturePath = filename;
		}
		return tex;
	}

	public void texture(Texture tex) {
		if (currentTexture != tex) {						// Only switch texture if it's not already pointing to the texture
			currentTexture = tex;
			this.set(TextureAttribute.createDiffuse(tex));
			texturePath = null;
		}
	}

	public void texture(TextureRegion texRegion) {
		if (currentTexture != texRegion.getTexture()) {		// Only switch texture if it's not already pointing to the texture
			currentTexture = texRegion.getTexture();
			this.set(TextureAttribute.createDiffuse(texRegion));
			texturePath = null;
		}
	}

	public void dispose() {
		if (shader != null)
			shader.dispose();
		shader = null;
		if (currentTexture != null)
			currentTexture.dispose();
		currentTexture = null;
	}

}
