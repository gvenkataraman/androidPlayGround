"""
Extract states from file and store it in another file
"""
import argparse
import csv


def run(input_file, output_file):
    with open(input_file, 'rb') as f_input:
        with open(output_file, 'wb') as f_output:
            reader = csv.reader(f_input, delimiter='\t')
            writer = csv.writer(f_output)
            states = set()
            for row_number, row in enumerate(reader):
                if row_number == 0:
                    continue
                state = row[3]
                if len(state) > 2 and (state not in states):
                    writer.writerow([state.lower()])
                    states.add(state)


def main():
    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument('--i', action='store', dest='input',)
    arg_parser.add_argument('--o', action='store', dest='output',)
    args = arg_parser.parse_args()
    run(input_file=args.input, output_file=args.output)


if __name__ == '__main__':
    main()
