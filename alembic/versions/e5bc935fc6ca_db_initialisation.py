"""DB initialisation

Revision ID: e5bc935fc6ca
Revises: 
Create Date: 2018-02-10 14:31:44.179032

"""

# revision identifiers, used by Alembic.
revision = 'e5bc935fc6ca'
down_revision = None
branch_labels = None
depends_on = None

from alembic import op
from sqlalchemy import Column
from sqlalchemy.dialects.postgresql import (BIGINT, INTEGER, VARCHAR, TIMESTAMP)

def upgrade():
    op.create_table('party',
                    Column('id', BIGINT, primary_key=True),
                    Column('key', VARCHAR, nullable=False),
                    Column('name', VARCHAR, nullable=False),
                    Column('created_at', TIMESTAMP, nullable=False),
                    schema='scalable')
    op.create_table('song',
                    Column('id', BIGINT, primary_key=True),
                    Column('streaming_service_id', VARCHAR, nullable=False),
                    Column('name', VARCHAR, nullable=False),
                    Column('artist', VARCHAR, nullable=False),
                    Column('album', VARCHAR, nullable=True),
                    Column('album_cover_url', VARCHAR, nullable=True),
                    Column('created_at', TIMESTAMP, nullable=False),
                    schema='scalable')
    op.create_table('party_queue',
                        Column('id', BIGINT, primary_key=True),
                        Column('party_id', BIGINT, nullable=False),
                        Column('song_id', BIGINT, nullable=False),
                        Column('upvotes', INTEGER, nullable=False, default=0),
                        Column('downvotes', INTEGER, nullable=True, default=0),
                        schema='scalable')

    op.create_foreign_key('party_queue_party_id_fk', 'party_queue', 'party',
                          ['party_id'], ['id'], None, None, None, None, None, 'scalable', 'scalable')
    op.create_foreign_key('party_queue_song_id_fk', 'party_queue', 'song',
                          ['song_id'], ['id'], None, None, None, None, None, 'scalable', 'scalable')
    op.create_index('party_queue_unique_constraint', 'party_queue',
                    ['party_id', 'song_id'], schema='scalable', unique=True)


def downgrade():
    op.drop_index('party_queue_unique_constraint', 'party_queue', schema='scalable')
    op.drop_constraint('party_queue_song_id_fk', 'party_queue', None, schema='scalable')
    op.drop_constraint('party_queue_party_id_fk', 'party_queue', None, schema='scalable')
    op.drop_table('party_queue', schema='scalable')
    op.drop_table('party', schema='scalable')
    op.drop_table('song', schema='scalable')
