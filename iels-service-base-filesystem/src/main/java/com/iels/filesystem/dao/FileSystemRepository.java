package com.iels.filesystem.dao;

import com.iels.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ClassName: FileSystemRepository
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/3 20
 * @Other:
 **/
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {
}
