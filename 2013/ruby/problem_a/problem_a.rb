require_relative '../test_solver'

class Board

  LINES = [
    [0,1,2,3],
    [4,5,6,7],
    [8,9,10,11],
    [12,13,14,15],
    [0,4,8,12],
    [1,5,9,13],
    [2,6,10,14],
    [3,7,11,15],
    [0,5,10,15],
    [3,6,9,12]
  ]

  PLAYERS = {
    'X' => ['X', 'T'],
    'O' => ['O', 'T']
  }
  EMPTY_CELL = '.'

  def self.create(lines, sample)
    self.new(lines.take(4).map{|s| s.split('')}.flatten)
  end

  def initialize(board)
    @board = board
  end

  def completed_line(symbols, line)
    line.all?{|index| symbols.include?(@board[index])}
  end

  def winner(symbol)
    player = PLAYERS[symbol]
    LINES.any?{|line| completed_line(player, line)}
  end

  def more_movements
    @board.any?{|cell| cell == '.'}
  end

  def lines_used
    5
  end
end

solve_file('a-sample.in', Board) do |board|
  if board.winner('X')
    "X won"
  elsif board.winner('O')
    "O won"
  elsif board.more_movements
    "Game has not completed"
  else
    "Draw"
  end
end
