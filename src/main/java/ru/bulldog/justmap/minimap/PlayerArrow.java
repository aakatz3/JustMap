package ru.bulldog.justmap.minimap;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix3f;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import ru.bulldog.justmap.JustMap;
import ru.bulldog.justmap.client.config.ClientParams;
import ru.bulldog.justmap.util.Colors;
import ru.bulldog.justmap.util.DrawHelper;
import ru.bulldog.justmap.util.ImageUtil;
import ru.bulldog.justmap.util.SpriteAtlas;

public class PlayerArrow extends Sprite {
	
	private final static TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
	private final static VertexFormat vertexFormat = new VertexFormat(ImmutableList.of(VertexFormats.POSITION_ELEMENT, VertexFormats.TEXTURE_ELEMENT, VertexFormats.NORMAL_ELEMENT, VertexFormats.PADDING_ELEMENT));
	
	private static PlayerArrow PLAYER_ARROW;
	
	private PlayerArrow(Identifier texture, int w, int h) {
		super(SpriteAtlas.MAP_ICONS, new Sprite.Info(texture, w, h, AnimationResourceMetadata.EMPTY), 0, w, h, 0, 0, ImageUtil.loadImage(texture, w, h));
	}
	
	public static void draw(int x, int y, float rotation) {
		if (!ClientParams.simpleArrow) {
			if (PLAYER_ARROW == null) {
				PLAYER_ARROW = new PlayerArrow(new Identifier(JustMap.MODID, "textures/icon/player_arrow.png"), 20, 20);
			}
			
			MatrixStack matrix = new MatrixStack();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();
			
			builder.begin(7, vertexFormat);
			
			VertexConsumer vertexConsumer = PLAYER_ARROW.getTextureSpecificVertexConsumer(builder);
			
			textureManager.bindTexture(PLAYER_ARROW.getId());
			
			RenderSystem.enableAlphaTest();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			matrix.push();
			matrix.translate(x, y, 0);
			matrix.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));
			
			Matrix4f m4f = matrix.peek().getModel();
			Matrix3f m3f = matrix.peek().getNormal();
			
			addVertices(m4f, m3f, vertexConsumer);
			tessellator.draw();
			
			matrix.pop();
		} else {
			int l = 6;
			double a1 = Math.toRadians((rotation + 90) % 360);
			double a2 = Math.toRadians((rotation - 45) % 360);
			double a3 = Math.toRadians((rotation + 225) % 360);
			
			int x1 = (int) (x + Math.cos(a1) * l);
			int y1 = (int) (y + Math.sin(a1) * l);
			int x2 = (int) (x + Math.cos(a2) * l);
			int y2 = (int) (y + Math.sin(a2) * l);
			int x3 = (int) (x + Math.cos(a3) * l);
			int y3 = (int) (y + Math.sin(a3) * l);
			
			DrawHelper.drawTriangle(x1, y1, x2, y2, x3, y3, Colors.RED);
		}
	}
	
	private static void addVertices(Matrix4f m4f, Matrix3f m3f, VertexConsumer vertexConsumer) {
		vertexConsumer.vertex(m4f, 5.0F, 5.0F, 0.0F).texture(0.0F, 0.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, 5.0F, -5.0F, 0.0F).texture(0.0F, 1.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, -5.0F, -5.0F, 0.0F).texture(1.0F, 1.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(m4f, -5.0F, 5.0F, 0.0F).texture(1.0F, 0.0F).normal(m3f, 0.0F, 1.0F, 0.0F).next();
	}
	
}
