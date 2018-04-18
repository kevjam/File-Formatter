# File-Formatter

## Overview
This program reads from a given input file and formats it to a given output file as to the user's specifications.

## Features
Options for the user are:
- Single or double spacing
- Left, right, or full justification
- Desired line width ranging from 20 to 100
- Display a popup with analysis of the files

## Functionality
The program will remove all extra spaces before performing its operation, and concatenates/splits lines to reach a desired line width.

**Full justification** is accomplished using a greedy algorithm, and spaces are added between words until the line length is reached. This could be improved using a dynamic programming approach, but several methods would have to be rewritten to take that into account.

If a word is greater than the desired line length, it will ignore any rules and be written onto its own line.
