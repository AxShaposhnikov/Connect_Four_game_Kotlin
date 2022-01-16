const val FIRST_PLAYER_DISC = 'o'
const val SECOND_PLAYER_DISC = '*'
const val LOWER_LEFT_CORNER = '\u255A'
const val LOWER_RIGHT_CORNER = '\u255D'
const val TRIPLE_JOINT = '\u2569'
const val HORIZONTAL_JOINT = '\u2550'
const val VERTICAL_JOINT = '\u2551'
const val WHITESPACE_CHAR = '\u0020'
const val DEFAULT_NUMBERS_OF_ROWS = 6
const val DEFAULT_NUMBERS_OF_COLUMNS = 7

fun drawBoard(columns: Int, board: MutableList<MutableList<Char>>) {
    for (i in 1..columns) if (i < columns) print(" $i") else print(" $i\n")

    for (row in board) {
        println(row.joinToString(""))
    }
}

fun buildBoard(rows: Int, columns: Int): MutableList<MutableList<Char>> {
    val boardWidth = columns * 2 + 1
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
            playerInput.lowercase() == "end" -> break
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

    //Call the winner verification function.
    if (playerInput != "end") {
        val win: Boolean = trackingMoves(playerInput, board, figure, rowForSearch)
        if (win) return "Player $playerName won"
    }

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
                var count = 1
                var searchCol = columnIndex

                //right direction
                while (count != 4) {
                    searchCol += columnStep
                    if (searchCol > board[row].size - columnStep) break
                    if (board[row][searchCol] == figure) count++ else break
                }

                searchCol = columnIndex

                //left direction
                while (count != 4) {
                    searchCol -= columnStep
                    if (searchCol < 1) break
                    if (board[row][searchCol] == figure) count++ else break
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
                var count = 1
                var searchRow = row

                while (count != 4) {
                    searchRow++
                    if (searchRow > board.size - columnStep) break
                    if (board[searchRow][columnIndex] == figure) count++ else break
                }

                searchRow = row

                while (count != 4) {
                    searchRow--
                    if (searchRow < 0) break
                    if (board[searchRow][columnIndex] == figure) count++ else break
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
                while (searchRow >= 0) {
                    if (searchCol > board[row].size - columnStep) break
                    if (board[searchRow][searchCol] == figure) count++ else break
                    searchRow--
                    searchCol += columnStep
                }

                //down-left
                if (count != 4) {
                    searchRow = row + 1
                    searchCol = columnIndex - columnStep

                    while (count != 4) {
                        if(searchRow > board.size - 2 || searchCol < 1) break
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

                    while (count != 4) {
                        if (searchRow < 0 || searchCol < 1) break
                        if (board[searchRow][searchCol] == figure) count++ else break
                        searchRow--
                        searchCol -= columnIndex
                    }
                }

                //down-right
                if (count != 4) {
                    searchRow = row + 1
                    searchCol = columnIndex + columnStep

                    while (count != 4) {
                        if (searchRow > board.size - 2 || searchCol > board[row].size - columnIndex )break
                        if (board[searchRow][searchCol] == figure) count++ else break
                        searchRow++
                        searchCol += columnIndex
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

fun startGameScreen (rows: Int, columns: Int, firstPlayerName: String ,secondPlayerName: String): Int {
    var numberOfGames: String
    while (true) {
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        numberOfGames = readLine()!!
        if (numberOfGames.toIntOrNull() != null) {
            if (numberOfGames.toInt() > 0) break else println("Invalid input")
        } else if (numberOfGames == "") break else println("Invalid input")
    }

    println("$firstPlayerName VS $secondPlayerName")
    println("$rows X $columns board")

    println(
        if (
            numberOfGames == "" ||
            numberOfGames.toIntOrNull() != null &&
            numberOfGames == "1") {
            "Single game"
        } else "Total $numberOfGames games\nGame #1"
    )

    return if (numberOfGames == "") 1 else numberOfGames.toInt()
}

fun playerNameRequest (prefix: String): String {
    var name: String
    do {
        println("$prefix player's name:")
        name = readLine()!!
    } while (name == "")
    return name
}

fun clearBoard(board: MutableList<MutableList<Char>>) {
    for (row in 1 until board.size - 1) {
        for (col in 1 until board[row].size - 1 step 2) {
            board[row][col] = WHITESPACE_CHAR
        }
    }
}

fun letsPlay(
    board: MutableList<MutableList<Char>>,
    firstPlayerName: String,
    secondPlayerName: String,
    columns: Int,
    firstPlayerFigure: Char,
    secondPlayerFigure: Char,
    numberOfGames: Int,
) {
    var countFirstPlayerMoves = 0
    var countSecondPlayerMoves = 0
    var status: String
    val multiplayer = numberOfGames > 1
    var firstPlayerPoints = 0
    var secondPlayerPoints = 0
    var gamesCount = 1

    fun check(playerMove: String): String {
        return when {
            playerMove.toIntOrNull() == null && playerMove.lowercase() != "end" -> {
                println(playerMove)
                "win"
            }
            countFirstPlayerMoves + countSecondPlayerMoves == (board.size - 1) * columns -> {
                println("It is a draw")
                "draw"
            }
            else -> "waitNextMove"
        }
    }


    AllGames@ for (i in 1..numberOfGames) {
        while (true) {
            val firstPlayerMove = makeMove(board, firstPlayerName, columns, firstPlayerFigure)
            if (firstPlayerMove.lowercase() == "end") break@AllGames
            countFirstPlayerMoves++

            status = check(firstPlayerMove)
            if (status == "win") {
                if (multiplayer) {
                    firstPlayerPoints += 2
                    println("Score")
                    println("$firstPlayerName: $firstPlayerPoints $secondPlayerName: $secondPlayerPoints")
                    break
                } else break@AllGames

            } else if (status == "draw") {
                if (multiplayer) {
                    firstPlayerPoints += 1
                    secondPlayerPoints += 1
                    println("Score")
                    println("$firstPlayerName: $firstPlayerPoints $secondPlayerName: $secondPlayerPoints")
                    break
                } else break@AllGames
            }

            val secondPlayerMove = makeMove(board, secondPlayerName, columns, secondPlayerFigure)
            if (secondPlayerMove.lowercase() == "end") break@AllGames
            countSecondPlayerMoves++

            status = check(secondPlayerMove)
            if (status == "win") {
                if (multiplayer) {
                    secondPlayerPoints += 2
                    println("Score")
                    println("$firstPlayerName: $firstPlayerPoints $secondPlayerName: $secondPlayerPoints")
                    break
                } else break@AllGames
            } else if (status == "draw") {
                if (multiplayer) {
                    firstPlayerPoints += 1
                    secondPlayerPoints += 1
                    println("Score")
                    println("$firstPlayerName: $firstPlayerPoints $secondPlayerName: $secondPlayerPoints")
                    break
                } else break@AllGames
            }
        }
        clearBoard(board)
        gamesCount++
        println("Game #$gamesCount")
        drawBoard(columns, board)
    }
    println("Game over!")
}

fun main() {
    println("Connect Four")
    val firstPlayerName: String = playerNameRequest("First")
    val secondPlayerName: String = playerNameRequest("Second")
    val board: MutableList<MutableList<Char>>
    var boardSize: String
    val rows: Int
    val columns: Int
    val numberOfGames: Int


    //Request to enter the size of the game board
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")

        boardSize = readLine()!!

        /* If the user has not entered the parameters, the board sizes are set by default.
        Otherwise, the input is checked for correctness*/
        if (boardSize == "") {
            columns = DEFAULT_NUMBERS_OF_COLUMNS
            numberOfGames = startGameScreen(DEFAULT_NUMBERS_OF_ROWS, columns, firstPlayerName, secondPlayerName)
            board = buildBoard(DEFAULT_NUMBERS_OF_ROWS, columns)
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

                            numberOfGames = startGameScreen(rows, columns, firstPlayerName, secondPlayerName)
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
    letsPlay(board, firstPlayerName, secondPlayerName, columns, FIRST_PLAYER_DISC, SECOND_PLAYER_DISC, numberOfGames)
}