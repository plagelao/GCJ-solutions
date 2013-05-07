require_relative '../test_solver'

class Pattern

  MAX_HEIGHT = 100
  MIN_HEIGHT = 1

  def self.create(lines, current)
    n, m = *lines.first.split(' ').map(&:to_i)
    pattern = lines.drop(1).take(n).map{|s| s.split(' ')}.flatten.map(&:to_i)
    self.new(n,m,pattern)
  end

  def initialize(n,m,pattern)
    @n, @m, @pattern = n, m, pattern.zip(0..n*m)
  end

  def is_possible?
     check_for_height(MIN_HEIGHT)
  end

  def check_for_height(height)
    if height > MAX_HEIGHT
      true
    else
      if all_parcels_are_correct?(parcels_with_height(height))
        check_for_height(height + 1)
      else
        false
      end
    end
  end

  def parcels_with_height(height)
    @pattern.select{|(h,_)| h == height}
  end

  def all_parcels_are_correct?(parcels)
    parcels.all?{|parcel| is_correct?(parcel)}
  end

  def is_correct?(parcel)
    height = parcel.first
    row = row_for(parcel.last)
    col = column_for(parcel.last)

    row_heights = @pattern.select do |h,i|
      row_for(i) == row && h <= height
    end
    column_heights = @pattern.select do |h,i|
      column_for(i) == col && h <= height
    end
    row_heights.size == @m || column_heights.size == @n
  end

  def row_for(index)
    index / @m
  end

  def column_for(index)
    (index % @m)
  end

  def lines_used
    @n + 1
  end
end

solve_file('B-large.in', Pattern) do |pattern|
  if pattern.is_possible?
    "YES"
  else
    "NO"
  end
end

