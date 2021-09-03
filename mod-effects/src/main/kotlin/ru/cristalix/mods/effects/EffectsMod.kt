package ru.cristalix.mods.effects

import dev.xdark.clientapi.event.input.KeyPress
import dev.xdark.clientapi.event.render.ScaleChange
import dev.xdark.clientapi.event.window.WindowResize
import dev.xdark.clientapi.item.Item
import dev.xdark.clientapi.item.ItemStack
import dev.xdark.clientapi.item.ItemTools
import dev.xdark.clientapi.opengl.GlStateManager
import dev.xdark.clientapi.resource.ResourceLocation
import dev.xdark.clientapi.util.EnumHand
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import ru.cristalix.clientapi.KotlinMod
import ru.cristalix.clientapi.readUtf8
import ru.cristalix.uiengine.UIEngine
import ru.cristalix.uiengine.element.RectangleElement
import ru.cristalix.uiengine.eventloop.Task
import ru.cristalix.uiengine.eventloop.animate
import ru.cristalix.uiengine.utility.*

class EffectsMod : KotlinMod() {

    lateinit var highlight: RectangleElement
    var highlightTask: Task? = null

    override fun onEnable() {
        UIEngine.initialize(this)
        highlight = rectangle {

            val resolution = clientApi.resolution()
            size.x = resolution.scaledWidth_double
            size.y = resolution.scaledHeight_double

            color = Color(60, 255, 60, 0.0)

            textureLocation = ResourceLocation.of("minecraft", "textures/misc/vignette.png")

            beforeRender = {
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
            }

            afterRender = {
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            }

        }

//        val cube = cube {
//            size = V3(30.0, 10.0, 14.0)
//            textureFrom = V2(0.0, 19.0)
//            textureSize = V2(128.0, 64.0)
//            textureLocation = clientApi.resourceManager().getLocation("minecraft", "textures/entity/chest/normal_double.png")
//
//            addChild(cube {
//                align = V3(0.5, 0.0, 1.0)
//                origin = V3(0.5, 1.0, 1.0)
//                offset = V3(0.0, 1.0, 0.0)
//                size = V3(30.0, 5.0, 14.0)
//                textureSize = V2(128.0, 64.0)
//                rotation = Rotation(-60.0 / 180.0 * Math.PI, 1.0, 0.0, 0.0)
//                textureLocation = clientApi.resourceManager().getLocation("minecraft", "textures/entity/chest/normal_double.png")
//
//                addChild(cube {
//                    align = V3(0.5, 1.0, 0.0)
//                    origin = V3(0.5, 0.0, 1.0)
//                    offset = V3(0.0, -2.0, 0.0)
//                    size = V3(2.0, 4.0, 1.0)
//                    textureSize = V2(128.0, 64.0)
//                    textureLocation = clientApi.resourceManager().getLocation("minecraft", "textures/entity/chest/normal_double.png")
//                })
//
//            })
//
//        }
//        val ctx = Context3D(V3(0.0, 100.0, 0.0))
//        ctx.addChild(cube)
//        UIEngine.worldContexts.add(ctx)

        registerHandler<ScaleChange> {
            highlight.size.x = clientApi.resolution().scaledWidth_double
            highlight.size.y = clientApi.resolution().scaledHeight_double
        }
        registerHandler<WindowResize> {
            highlight.size.x = resolution.scaledWidth_double
            highlight.size.y = resolution.scaledHeight_double
        }

        UIEngine.overlayContext.addChild(highlight)


        val title = text {
            content = ""
//            offset.z = -172.0
            beforeRender {
                GlStateManager.disableDepth()
            }
            align = V3(0.5, 0.6)
            origin = BOTTOM
            scale = V3(0.0, 0.0, 0.0)
            shadow = true
        }
        val subtitle = text {
            content = ""
//            offset.z = -172.0
            align = V3(0.5, 0.6)
            afterRender {
                GlStateManager.enableDepth()
            }
            offset.y = 1.0
            origin = TOP
            scale = V3(0.0, 0.0, 0.0)
            shadow = true
        }

        UIEngine.overlayContext.addChild(title, subtitle)


        registerChannel("highlight") {
            highlightTask?.cancelled = true

            highlight.animate(0.5, Easings.QUART_OUT) {
                color.alpha = 0.65
            }

            UIEngine.schedule(0.6) {
                highlight.animate(0.4, Easings.QUART_OUT) {
                    color.alpha = 0.0
                }
            }
        }

        registerChannel("itemtitle") {

            clientApi.overlayRenderer().displayItemActivation(ItemTools.read(this))
            title.content = readUtf8()
            subtitle.content = readUtf8()
            title.animate(1.0, Easings.ELASTIC_OUT) {
                scale.x = 4.0
                scale.y = 4.0
            }
            subtitle.animate(0.5, Easings.ELASTIC_OUT) {
                scale.x = 2.0
                scale.y = 2.0
            }
            UIEngine.schedule(2) {
                title.animate(0.25) {
                    scale.x = 0.0
                    scale.y = 0.0
                }
                subtitle.animate(0.25) {
                    scale.x = 0.0
                    scale.y = 0.0
                }
            }
        }

//        registerHandler<KeyPress> {
//            when (key) {
//
//                Keyboard.KEY_J -> {
//                    highlightTask?.cancelled = true
//
//                    highlight.animate(0.5, Easings.QUART_OUT) {
//                        color.alpha = 0.65
//                    }
//
//                    UIEngine.schedule(0.6) {
//                        highlight.animate(0.4, Easings.QUART_OUT) {
//                            color.alpha = 0.0
//                        }
//                    }
//                }
//
//                Keyboard.KEY_K -> {
//
//                    clientApi.overlayRenderer().displayItemActivation(ItemStack.of(Item.of(54), 1, 0))
//
//                }
//
//                Keyboard.KEY_PAUSE -> {
//                    unload()
//                }
//            }
//
//
//        }


    }

}
