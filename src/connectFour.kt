const val FIRST_PLAYER_DISC = 'o'
const val SECOND_PLAYER_DISC = '*'
const val LOWER_LEFT_CORNER = '\u255A'
const val LOWER_RIGHT_CORNER = '\u255D'
const val TRIPLE_JOINT = '\u2569'
const val HORIZONTAL_JOINT = '\u2550'
const val VERTICAL_JOINT = '\u2551'
const val WHITESPACE_CHAR = '\u0020'

fun drawBoard(columns: Int, board: MutableList<MutableList<Char>>) {
    for (i in 1..columns) if (i < columns) print(" $i") else print(" $i\n")

    for (row in board) {
        println(row.joinToString(""))
    }
}

fun buildBoard(rows: Int, cols: Int): MutableList<MutableList<Char>> {
    val boardWidth = cols * 2 + 1
    val board = MutableList(rows + 1) { MutableList(boardWidth) {WHITESPACE_CHAR} }

    for (i in 0..rows) {
        if (i == rows) {
            for (j in 0 until boardWidth) {
                when {
                    j == 0 -> board[i][j] = LOWER_LEFT_CORNER
                    j == boardWidth - 1 -> board[i][j] = LOWER_RIGHT_CORNER
                    j in 2..boardWidth - 2 && j % 2 == 0 -> board[i][j] = TRIPLE_JOINT
                    else -> board[i][j] = HORIZONTAL_JOINT
                }
            }
        } else {
            for (j in 0..boardWidth step 2) board[i][j] = VERTICAL_JOINT
        }
    }

    return board
}

fun makeMove(board: MutableList<MutableList<Char>>, playerName: String, columns: Int, figure: Char): String {
    var playerInput: String
    var rowForSearch = 0

    while (true) {
        println("$playerName\'s turn:")
        playerInput = readLine()!!

        when {
            playerInput.lowercase() == "end" -> {
                println("Game over!")
                break
            }
            playerInput.toIntOrNull() == null -> println("Incorrect column number")
            playerInput.toIntOrNull() != null && playerInput.toInt() !in 1..columns -> {
                println("The column number is out of range (1 - $columns)")
            }
            else -> {
                val colIndex = playerInput.toInt() * 2 - 1
                if (board[0][colIndex] != WHITESPACE_CHAR) {
                    println("Column $playerInput is full")
                } else {
                    for (row in board.size - 1 downTo 0) {
                        if (board[row][colIndex] != WHITESPACE_CHAR) {
                            continue
                        } else {
                            board[row][colIndex] = figure
                            rowForSearch = row
                            break
                        }
                    }
                    drawBoard(columns, board)
                    break
                }
            }
        }
    }

    //Вызвать функцию проверки победителя.
    //trackingMoves(playerInput, board, figure, rowForSearch)

    return playerInput
}

fun trackingMoves(move: String, board: MutableList<MutableList<Char>>, figure: Char, row: Int): Boolean {
    val columnIndex = move.toInt() * 2 - 1
    val columnStep = 2
    var result: Boolean

    fun horizontalSearch(): Boolean {
        val winner: Boolean
        when (columnIndex) {
            1 -> {
                var count = 1
                for (i in columnIndex + columnStep until columnStep * 4 step columnStep) {
                    if (board[row][i] == figure) count++
                }
                winner = count == 4
            }
            board[row].size - columnStep -> {
                var count = 1
                for (i in columnIndex - columnStep downTo columnIndex - columnStep * 3 step columnStep) {
                    if (board[row][i] == figure) count++
                }
                winner = count == 4
            }
            else -> {
                var index = columnIndex
                var count = 1

                while (index <= board[row].size - columnStep || count != 4) {
                    index += columnStep
                    if (board[row][index] == figure) count++ else break
                }

                index = columnIndex

                while (index >= 1 || count != 4) {
                    index -= columnStep
                    if (board[row][index] == figure) count++ else break
                }

                winner = count == 4
            }
        }
        return winner
    }

    fun verticalSearch(): Boolean {
        val winner: Boolean
        when (row) {
            board.size - columnStep -> {
                var count = 1
                for (i in row - 1 downTo row - 3) {
                    if (board[i][columnIndex] == figure) count++
                }
                winner = count == 4
            }
            0 -> {
                var count = 1
                for (i in 1..3) {
                    if (board[i][columnIndex] == figure) count++
                }
                winner = count == 4
            }
            else -> {
                var index = row
                var count = 1

                while (index <= board.size - columnStep || count != 4) {
                    index++
                    if (board[index][columnIndex] == figure) count++ else break
                }

                index = row

                while (index >= 0 || count != 4) {
                    index--
                    if (board[index][columnIndex] == figure) count++ else break
                }

                winner = count == 4
            }
        }
        return winner
    }

    fun diagonalSearch(): Boolean {
        val winner: Boolean
        when {
            //lower left corner
            row == board.size - columnStep && columnIndex == 1 -> {
                var count = 1
                var searchRow = row - 1
                for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                    if (board[searchRow][i] == figure) count++
                    searchRow--
                }
                winner = count == 4
            }
            //lower right corner
            row == board.size - columnStep && columnIndex == board[row].size - columnStep -> {
                var count = 1
                var searchRow = row - 1
                for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                    if (board[searchRow][i] == figure) count++
                    searchRow--
                }
                winner = count == 4
            }
            //upper left corner
            row == 0 - columnStep && columnIndex == 1 -> {
                var count = 1
                var searchRow = 1
                for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                    if (board[searchRow][i] == figure) count++
                    searchRow++
                }
                winner = count == 4
            }
            //upper right corner
            row == 0 && columnIndex == board[row].size - columnStep -> {
                var count = 1
                var searchRow = 1
                for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                    if (board[searchRow][i] == figure) count++
                    searchRow++
                }
                winner = count == 4
            }
            //lower border
            row == board.size - columnStep && columnIndex in 3..board[row].size - columnStep * 2 -> {
                var count = 1
                var searchRow = row - 1

                //right direction search
                if (columnIndex + columnStep * 3 <= board[row].size - columnStep) {
                    for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                        if (board[searchRow][i] == figure) count++
                        searchRow--
                    }
                }

                //left direction search
                if (count != 4 && columnIndex - columnStep * 3 >= 1) {
                    count = 1
                    searchRow = row - 1
                    for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                        if (board[searchRow][i] == figure) count++
                        searchRow--
                    }
                }

                winner = count == 4
            }
            //left border
            row in 1 until board.size - columnStep && columnIndex == 1 -> {
                var count = 1
                var searchRow = row

                //up direction search
                if (row - 3 >= 0) {
                    for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                        searchRow--
                        if (board[searchRow][i] == figure) count++
                    }
                }
                //down direction search
                if (count != 4 && row + 3 <= board.size - 2) {
                    count = 1
                    searchRow = row
                    for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                        searchRow++
                        if (board[searchRow][i] == figure) count++
                    }
                }

                winner = count == 4
            }
            //upper border
            row == 0 && columnIndex in 3..board[row].size - columnStep * 2 -> {
                var count = 1
                var searchRow = 1

                //right direction search
                if (columnIndex + columnStep * 3 <= board[row].size - columnStep) {
                    for (i in columnIndex + columnStep..columnIndex + columnStep * 3 step columnStep) {
                        if (board[searchRow][i] == figure) count++
                        searchRow++
                    }
                }
                //left direction search
                if (count != 4 && columnIndex - columnStep * 3 >= 1) {
                    count = 1
                    searchRow = 1
                    for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                        if (board[searchRow][i] == figure) count++
                        searchRow++
                    }
                }
                winner = count == 4
            }
            //right border
            row in 1 until board.size - columnStep && columnIndex == board[row].size - columnStep -> {
                var count = 1
                var searchRow = row

                //up direction search
                if (row - 3 >= 0) {
                    for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                        searchRow--
                        if (board[searchRow][i] == figure) count++
                    }
                }
                //down direction search
                if (count != 4 && row + 3 <= board.size - 2) {
                    count = 1
                    searchRow = row
                    for (i in columnIndex - columnStep downTo  columnIndex - columnStep * 3 step columnStep) {
                        searchRow++
                        if (board[searchRow][i] == figure) count++
                    }
                }
                winner = count == 4
            }
            else -> {
                var count = 1
                var searchRow = row - 1
                var searchCol = columnIndex + columnStep

                //1st part: up-right & down-left
                //up-right
                while (searchRow >= 0 || searchCol <= board[row].size - columnStep) {
                    if (board[searchRow][searchCol] == figure) count++ else break
                    searchRow--
                    searchCol += columnStep
                }

                //down-left
                if (count != 4) {
                    searchRow = row + 1
                    searchCol = columnIndex - columnStep

                    while (searchRow <= board.size - 2 || searchCol >= 1 || count != 4) {
                        if (board[searchRow][searchCol] == figure) count++ else break
                        searchRow++
                        searchCol -= columnStep
                    }
                }

                //2nd part: up-left & down-right

                //up-left
                if (count != 4) {
                    count = 1
                    searchRow = row - 1
                    searchCol = columnIndex - columnStep

                    while (searchRow >= 0 || searchCol >= 1 || count != 4) {
                        if (board[searchRow][searchCol] == figure) count++ else break
                        searchRow--
                        searchCol -= columnIndex
                    }
                }

                //down-right
                if (count != 4) {
                    searchRow = row + 1
                    searchCol = columnIndex + columnStep

                    while (searchRow <= board.size - 2 || searchCol <= board[row].size - columnIndex || count != 4) {
                        if (board[searchRow][searchCol] == figure) count++ else break
                        searchRow--
                        searchCol -= columnIndex
                    }
                }

                winner = count == 4
            }
        }
        return winner
    }

    result = horizontalSearch()
    if (!result) result = verticalSearch()
    if (!result) result = diagonalSearch()

    return result
}

fun letsPlay(
    board: MutableList<MutableList<Char>>,
    firstPlayerName: String,
    secondPlayerName: String,
    columns: Int,
    firstPlayerFigure: Char,
    secondPlayerFigure: Char,
) {
    while (true) {
        val firstPlayerOut = makeMove(board, firstPlayerName, columns, firstPlayerFigure)
        if (firstPlayerOut.lowercase() == "end") break
        val secondPlayerOut = makeMove(board, secondPlayerName, columns, secondPlayerFigure)
        if (secondPlayerOut.lowercase() == "end") break
    }
}

fun main() {
    println("Connect Four")
    val board: MutableList<MutableList<Char>>
    var firstPlayerName: String
    var secondPlayerName: String
    var boardSize: String
    val rows: Int
    val columns: Int

    /*Request to enter the name of the first player
    The player's name cannot be empty*/
    while (true) {
        println("First player's name:")
        firstPlayerName = readLine()!!
        if (firstPlayerName != "") break
    }

    /*Request to enter the name of the second player
    The player's name cannot be empty*/
    while (true) {
        println("Second player's name:")
        secondPlayerName = readLine()!!
        if (secondPlayerName != "") break
    }

    //Request to enter the size of the game board
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")

        boardSize = readLine()!!

        /* If the user has not entered the parameters, the board sizes are set by default.
        Otherwise, the input is checked for correctness*/

        if (boardSize == "") {
            rows = 6
            columns = 7

            println("$firstPlayerName VS $secondPlayerName")
            println("$rows X $columns board")

            /*The drawBoard function of the same name draws a game board.
            The game board is drawn with default parameters*/
            board = buildBoard(6, 7)
            drawBoard(columns, board)


            break
        } else {
            boardSize = boardSize.replace("\\s".toRegex(), "")
            boardSize = boardSize.lowercase()

            if ("x" in boardSize) {
                val tmpValues = boardSize.split("x")

                if (tmpValues.size != 2) {
                    println("Invalid input")
                } else {
                    if (tmpValues[0].toIntOrNull() == null || tmpValues[1].toIntOrNull() == null) {
                        println("Invalid input")
                    } else {
                        if (tmpValues[0].toInt() !in 5..9 && tmpValues[1].toInt() !in 5..9) {
                            println("Board rows should be from 5 to 9")
                            println("Board columns should be from 5 to 9")
                        } else if (tmpValues[0].toInt() !in 5..9 && tmpValues[1].toInt() in 5..9) {
                            println("Board rows should be from 5 to 9")
                        } else if (tmpValues[0].toInt() in 5..9 && tmpValues[1].toInt() !in 5..9) {
                            println("Board columns should be from 5 to 9")
                        } else if (tmpValues[0].toInt() in 5..9 && tmpValues[1].toInt() in 5..9) {
                            rows = tmpValues[0].toInt()
                            columns = tmpValues[1].toInt()

                            println("$firstPlayerName VS $secondPlayerName")
                            println("$rows X $columns board")

                            /*The drawBoard function of the same name draws a game board.
                            At this point, the parameter input check was successful*/
                            board = buildBoard(rows, columns)
                            drawBoard(columns, board)

                            break
                        }
                    }
                }
            } else {
                println("Invalid input")
            }
        }
    }

    letsPlay(board, firstPlayerName, secondPlayerName, columns, FIRST_PLAYER_DISC, SECOND_PLAYER_DISC)
}