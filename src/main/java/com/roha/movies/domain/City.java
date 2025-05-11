package com.roha.movies.domain;


public record City(String id, String href, String name) {
    public static class CityBuilder {
        private String id;
        private String href;
        private String name;

        public CityBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CityBuilder href(String href) {
            this.href = href;
            return this;
        }
         public CityBuilder name(String name) {
            this.name = name;
            if(this.id == null && this.name != null) {
                this.id = IdCreator.create(this.name);
            }
            return this;
         }
         public City build() {
            return new City(id, href, name);
         }
    }
}

