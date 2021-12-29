ALTER TABLE block_history
    ADD CONSTRAINT FK_BLOCK_HISTORY_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE block_history
    ADD CONSTRAINT FK_BLOCK_HISTORY_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;
ALTER TABLE block_history
    ADD CONSTRAINT FK_BLOCK_HISTORY_ON_POST_COMMENT FOREIGN KEY (post_comment_id) REFERENCES post_comments (id) ON DELETE CASCADE;

ALTER TABLE friendship
    ADD CONSTRAINT uc_friendship_status UNIQUE (status_id);
ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_DSTPERSON FOREIGN KEY (dst_person_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_SRCPERSON FOREIGN KEY (src_person_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_STATUS FOREIGN KEY (status_id) REFERENCES friendship_statuses (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES persons (id) ON DELETE CASCADE;

ALTER TABLE notification_type
    ADD CONSTRAINT FK_NOTIFICATION_TYPE_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE;

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE;

ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_PARENT FOREIGN KEY (parent_id) REFERENCES post_comments (id) ON DELETE CASCADE;
ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE post_files
    ADD CONSTRAINT FK_POST_FILES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;
ALTER TABLE post_likes
    ADD CONSTRAINT FK_POST_LIKES_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE post_likes
    ADD CONSTRAINT FK_POST_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;
ALTER TABLE posts
    ADD CONSTRAINT uc_posts_block UNIQUE (block_id);
ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES persons (id) ON DELETE CASCADE;
ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_BLOCK FOREIGN KEY (block_id) REFERENCES block_history (id) ON DELETE CASCADE;
ALTER TABLE post2tag
    ADD CONSTRAINT FK_POST_ON_TAG FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE;
ALTER TABLE post2tag
    ADD CONSTRAINT FK_TAG_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;
