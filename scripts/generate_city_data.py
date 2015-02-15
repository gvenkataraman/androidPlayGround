"""
Read max mind data and generate city data
We need csv with
name, postal code, State
"""
from __future__ import unicode_literals

import argparse
import csv


def run(input_file, output_file):
    with open(input_file, 'rb') as f_input:
        with open(output_file, 'wb') as f_output:
            reader = csv.reader(f_input)
            writer = csv.writer(f_output)
            for row_number, row in enumerate(reader):
                if row_number == 0:
                    continue
                writer.writerow([row[1], row[3], row[4]])


def main():
    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument('--i', action='store', dest='input',)
    arg_parser.add_argument('--o', action='store', dest='output',)
    args = arg_parser.parse_args()
    run(input_file=args.input, output_file=args.output)


if __name__ == '__main__':
    main()
