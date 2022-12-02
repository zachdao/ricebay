import React, { useCallback, useState } from 'react';
import {
    TableView,
    TableBody,
    TableHeader,
    Row,
    Cell,
    Column,
    Tooltip,
    TooltipTrigger,
    ActionButton,
    useAsyncList,
    Flex,
    Heading,
} from '@adobe/react-spectrum';
import { DateTime } from 'luxon';

export const BidHistory = ({ auction, ...otherParams }) => {
    const list = useAsyncList({
        async load() {
            return {
                items:
                    auction?.bids?.map((bid, idx) => ({
                        ...bid,
                        id: idx,
                        timestamp: DateTime.fromFormat(bid.timestamp, 'DD'),
                    })) || [],
            };
        },
        async sort({ items, sortDescriptor }) {
            const { column, direction } = sortDescriptor;
            const sorted = items.sort((a, b) => {
                const first = direction === 'ascending' ? a : b;
                const second = direction === 'ascending' ? b : a;
                if (column === 'alias') {
                    console.log('sorting by alias');
                    return first.alias.localeCompare(second.alias);
                }

                return first[column] - second[column];
            });
            return { items: sorted };
        },
        initialSortDescriptor: { column: 'timestamp', direction: 'descending' },
    });

    return (
        <Flex
            direction="column"
            justifyContent="start"
            alignItems="start"
            {...otherParams}
        >
            <Heading>Bid History</Heading>
            <TableView
                width="100%"
                selectionMode="none"
                aria-label="bid history"
                height="size-3000"
                renderEmptyState={() => <em>No Bids</em>}
                onSortChange={list.sort}
                sortDescriptor={list.sortDescriptor}
            >
                <TableHeader
                    columns={[
                        { name: 'Bidder', uid: 'alias' },
                        { name: 'Amount', uid: 'bid' },
                        { name: 'Date', uid: 'timestamp' },
                    ]}
                >
                    {(column) => (
                        <Column
                            allowsSorting
                            key={column.uid}
                            align={column.uid === 'timestamp' ? 'end' : 'start'}
                        >
                            {column.name}
                        </Column>
                    )}
                </TableHeader>
                <TableBody items={list.items} loadingState={list.loadingState}>
                    {(item) => (
                        <Row key={item.id}>
                            <Cell>{item.alias}</Cell>
                            <Cell>${item.bid.toFixed(2)}</Cell>
                            <Cell>
                                <TooltipTrigger>
                                    <ActionButton isQuiet>
                                        {item.timestamp.toRelative()}
                                    </ActionButton>
                                    <Tooltip>
                                        {item.timestamp.toFormat(
                                            'LLL dd, yyyy',
                                        )}
                                    </Tooltip>
                                </TooltipTrigger>
                            </Cell>
                        </Row>
                    )}
                </TableBody>
            </TableView>
        </Flex>
    );
};
