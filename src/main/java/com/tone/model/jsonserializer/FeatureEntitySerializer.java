package com.tone.model.jsonserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tone.model.FeatureEntity;

public class FeatureEntitySerializer extends StdSerializer<FeatureEntity> {

	private static final long serialVersionUID = 1L;

	public FeatureEntitySerializer() {
        this(null);
    }
 
    public FeatureEntitySerializer(Class<FeatureEntity> t) {
        super(t);
    }

	@Override
	public void serialize(FeatureEntity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
		FeatureEntity feature = new FeatureEntity();
		feature.setId(value.getId());		
		feature.setName(value.getName());
		feature.setStatus(value.getStatus());
		feature.setType(value.getType());
		
		gen.writeObject(feature);
		
	}

}
