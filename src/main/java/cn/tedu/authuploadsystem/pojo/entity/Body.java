package cn.tedu.authuploadsystem.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Body implements Serializable {
    private List<Tag> Tags;
}
