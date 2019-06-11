package checkers

import scala.util.Random


object ZobristHash {

	private final val rand = new Random(1)

	final val zobrist: Map[(Pos, Option[Piece]), Long] = {
		val allPieces = List(None, Some(Piece(Black, Pawn)), Some(Piece(Black, Queen)),
									Some(Piece(White, Pawn)), Some(Piece(White, Queen)))

		val seq = for (m <- 1 to 50; piece <- allPieces) yield ((Pos.posAt(m).get, piece) -> rand.nextLong())
		seq.toMap
	}

	def computeHash(map: Map[Pos, Piece]): Long = {
		val longs = for (m <- 1 to 50) yield {
			val pos = Pos.posAt(m).get
			if (map contains pos) zobrist((pos, map.get(pos)))
			else zobrist((pos, None))
		}

		longs.foldLeft(0L) { (acc, elt) => acc ^ elt }
	}

	def replace(prevHash: Long, pos: Pos, oldPiece: Option[Piece], newPiece: Option[Piece]): Long = {
		prevHash ^ zobrist((pos, oldPiece)) ^ zobrist((pos, newPiece))
	}

}
