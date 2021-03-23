package com.stash;

import io.debezium.spi.converter.CustomConverter;
import io.debezium.spi.converter.RelationalColumn;
import org.apache.kafka.connect.data.SchemaBuilder;
import java.util.Properties;

public class MoneyConverter implements CustomConverter<SchemaBuilder, RelationalColumn> {

    private SchemaBuilder moneySchema;

    @Override
    public void configure(Properties props) {
        moneySchema = SchemaBuilder.string().name(props.getProperty("schema.name"));
    }

    @Override
    public void converterFor(RelationalColumn column,
                             CustomConverter.ConverterRegistration<SchemaBuilder> registration) {

        if ("money".equals(column.typeName())) {
            registration.register(moneySchema, data -> {
                if (data == null) {
                    if (column.isOptional()) {
                        return null;
                    }
                }

                return String.format("%.2f", data);
            });
        }
    }
}