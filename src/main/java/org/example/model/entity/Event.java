package org.example.model.entity;

import lombok.Builder;

@Builder
public record Event(
        Long id,
        String title,
        String description,
        String metroStation,
        String city,
        String uri,
        String price
) {
    @Override
    public String toString() {
        return String.format("'%s', '%s', '%s', '%s', '%s'", title, description, metroStation, city, uri, price);
    }
    public String getFormattedAdvertisementInfo() {
        return String.format("""
                Событие '%s'!
                
                Город: %s
                Описание события: %s.
                Метро: %s.
                Ссылка на событие: %s
                
                Стоимость: %s
                """, title, city, description, metroStation, uri, price);
    }
}
