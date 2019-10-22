package chess

import scala.math.abs


case class History(whiteShortCastlingAllowed: Boolean = true,
					whiteLongCastlingAllowed: Boolean = true,
					blackShortCastlingAllowed: Boolean = true,
					blackLongCastlingAllowed: Boolean = true,
					enPassant: Option[Pos] = None) {
	
	def allowed(color: Color, longSide: Boolean): Boolean = {
		if (color == White) {
			if (longSide) whiteLongCastlingAllowed else whiteShortCastlingAllowed
		} else {
			if (longSide) blackLongCastlingAllowed else blackShortCastlingAllowed
		}
	}

	def allowed(color: Color, pawnPos: Pos): Boolean = {
		if (enPassant.isEmpty) false
		else {
			val target = enPassant.get
			if (color == Black && target.y == 3 && abs(pawnPos.x - target.x) == 1) true
			else if (color == White && target.y == 6 && abs(pawnPos.x - target.x) == 1) true
			else false
		}
	}

	def kingMoved(color: Color): History = {
		if (color == White) {
			copy(whiteShortCastlingAllowed = false, whiteLongCastlingAllowed = false)
		} else {
			copy(blackShortCastlingAllowed = false, blackLongCastlingAllowed = false)
		}
	}

	def rookMoved(color: Color, origin: Pos): History = {
		if (color == White) {
			if (origin.x == 1) copy(whiteLongCastlingAllowed = false)
			else if (origin.x == 8) copy(whiteShortCastlingAllowed = false)
			else this
		} else {
			if (origin.x == 1) copy(blackLongCastlingAllowed = false)
			else if (origin.x == 8) copy(blackShortCastlingAllowed = false)
			else this
		}
	}

	def pawnMovedBy2(color: Color, x: Int): History = {
		val target =
			if (color == White) Pos.posAt(x, 3)
			else if (color == Black) Pos.posAt(x, 6)
			else None

		if (target == enPassant) this
		else copy(enPassant = target)
	}

	def resetEnPassant: History = {
		if (enPassant == None) this
		else copy(enPassant = None)
	}

}
