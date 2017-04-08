package com.chewbyte.offpeaky.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowListProcessor;

public class CsvSearch extends RowListProcessor {
    //value to be searched for
    private final String stringToMatch;

    //name of column to match (if you don't have headers)
    private final String columnToMatch;

    //position of column to match
    private int indexToMatch = -1;

    public CsvSearch(String columnToMatch, String stringToMatch){
        this.columnToMatch = columnToMatch;
        this.stringToMatch = stringToMatch.toLowerCase(); //lower case to make the search case-insensitive
    }

    public CsvSearch(int columnToMatch, String stringToMatch){
        this(stringToMatch, null);
        this.indexToMatch = columnToMatch;
    }

    @Override
    public void rowProcessed(String[] row, ParsingContext context) {
        if(indexToMatch == -1) {
            indexToMatch = context.indexOf(columnToMatch);
        }

        String value = row[indexToMatch];
        if(value != null && value.toLowerCase().contains(stringToMatch)) {
            super.rowProcessed(row, context); // default behavior of the RowListProcessor: add the row into a List.
        }
        // else skip the row.
    }
}