package com.net3hings.triviagameapp

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import com.net3hings.triviagameapp.question.Question
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.component.OverlayingComponent
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.views.dimensions.dimensionsOf

object Helper {
	fun resolveCategory(category: Int): String {
		return when(category) {
			0 -> "Any Category"
			9 -> "General Knowledge"
			10 -> "Entertainment: Books"
			11 -> "Entertainment: Film"
			12 -> "Entertainment: Music"
			13 -> "Entertainment: Musicals &amp; Theatres"
			14 -> "Entertainment: Television"
			15 -> "Entertainment: Video Games"
			16 -> "Entertainment: Board Games"
			17 -> "Science &amp; Nature"
			18 -> "Science: Computers"
			19 -> "Science: Mathematics"
			20 -> "Mythology"
			21 -> "Sports"
			22 -> "Geography"
			23 -> "History"
			24 -> "Politics"
			25 -> "Art"
			26 -> "Celebrities"
			27 -> "Animals"
			28 -> "Vehicles"
			29 -> "Entertainment: Comics"
			30 -> "Science: Gadgets"
			31 -> "Entertainment: Japanese Anime &amp; Manga"
			32 -> "Entertainment: Cartoon &amp; Animations"
			else -> "Unknown"
		}
	}

	fun resolveDifficulty(difficulty: Question.Difficulty): String {
		return when(difficulty) {
			Question.Difficulty.ANY -> "0"
			Question.Difficulty.EASY -> "easy"
			Question.Difficulty.MEDIUM -> "medium"
			Question.Difficulty.HARD -> "hard"
		}
	}

	fun resolveType(type: Question.Type): String {
		return when(type) {
			Question.Type.ANY -> "0"
			Question.Type.MULTIPLE -> "multiple"
			Question.Type.BOOLEAN -> "boolean"
		}
	}

	fun getAttributeValue(context: Context, attr: Int): Int? {
		return TypedValue()
			.apply { context.theme.resolveAttribute(attr, this, true) }
			.resourceId
			.takeUnless { it == 0 }
	}

	private val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
	private val LABEL_BACKGROUND_SHADOW_DY = 2f
	private val LABEL_LINE_COUNT = 1
	private val GUIDELINE_ALPHA = .2f
	private val INDICATOR_SIZE_DP = 36f
	private val INDICATOR_OUTER_COMPONENT_ALPHA = 32
	private val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
	private val GUIDELINE_DASH_LENGTH_DP = 8f
	private val GUIDELINE_GAP_LENGTH_DP = 4f
	private val SHADOW_RADIUS_MULTIPLIER = 1.3f

	private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
	private val labelHorizontalPaddingValue = 8f
	private val labelVerticalPaddingValue = 4f
	private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
	private val indicatorInnerAndCenterComponentPaddingValue = 5f
	private val indicatorCenterAndOuterComponentPaddingValue = 10f
	private val guidelineThickness = 2f
	private val guidelineShape = DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)

	fun makeMarker(context: Context): Marker {
		val labelBackgroundColor = context.getColor(
			Helper.getAttributeValue(
				context,
				com.google.android.material.R.attr.colorSurface
			)!!
		)
		val labelBackground = ShapeComponent(labelBackgroundShape, labelBackgroundColor).setShadow(
			radius = LABEL_BACKGROUND_SHADOW_RADIUS,
			dy = LABEL_BACKGROUND_SHADOW_DY,
			applyElevationOverlay = true,
		)

		val label = textComponent {
			background = labelBackground
			lineCount = LABEL_LINE_COUNT
			padding = labelPadding
			typeface = Typeface.MONOSPACE
		}

		val indicatorInnerComponent = ShapeComponent(Shapes.pillShape, labelBackgroundColor)
		val indicatorCenterComponent = ShapeComponent(Shapes.pillShape, context.getColor(R.color.white))
		val indicatorOuterComponent = ShapeComponent(Shapes.pillShape, context.getColor(R.color.white))
		val indicator = OverlayingComponent(
			outer = indicatorOuterComponent,
			inner =
			OverlayingComponent(
				outer = indicatorCenterComponent,
				inner = indicatorInnerComponent,
				innerPaddingAllDp = indicatorInnerAndCenterComponentPaddingValue,
			),
			innerPaddingAllDp = indicatorCenterAndOuterComponentPaddingValue,
		)
		val guideline = LineComponent(
			com.google.android.material.R.attr.colorOnSurface.copyColor(GUIDELINE_ALPHA),
			guidelineThickness,
			guidelineShape,
		)

		return object: MarkerComponent(label, indicator, guideline) {
			init {
				indicatorSizeDp = INDICATOR_SIZE_DP
				onApplyEntryColor = { entryColor ->
					indicatorOuterComponent.color = entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
					with(indicatorCenterComponent) {
						color = entryColor
						setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
					}
				}
			}

			override fun getInsets(
				context: MeasureContext,
				outInsets: Insets,
				horizontalDimensions: HorizontalDimensions,
			) = with(context) {
				outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
						LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
						LABEL_BACKGROUND_SHADOW_DY.pixels
			}
		}
	}

	fun convertToListOfFloatEntries(items: MutableList<Double>): List<FloatEntry> {
		val newList: MutableList<FloatEntry> = mutableListOf()

		for((index, value) in items.withIndex())
			newList.add(entryOf(index + 1, value))

		return newList
	}

}