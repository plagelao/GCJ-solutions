def solve_file(file, test_class, &block)
  File.open(file.gsub('.in','.out'), 'w') do |out|
    solution = []
    test_cases, *lines = *File.open(file).read.split("\n")
    test_cases.to_i.times do |sample|
      stuff = test_class.create(lines, sample)
      lines = lines.drop(stuff.lines_used)
      state = block.call(stuff)
      solution << "Case ##{sample+1}: #{state}"
    end
    out.write solution.join("\n")
    puts solution.inspect
  end
end
