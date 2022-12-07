import React, { useEffect } from 'react';
import {
    Flex,
    Heading,
    TableView,
    TableHeader,
    Column,
    TableBody,
    Row,
    Cell,
} from '@adobe/react-spectrum';
import { useHttpQuery } from '../http-query/use-http-query';
import { useNavigate } from 'react-router-dom';
import { Link } from '@adobe/react-spectrum';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const MyAuctions = () => {
    const columns = [
        { name: 'Auction Title', uid: 'title' },
        { name: 'Auction Start Date', uid: 'startDate' },
        { name: 'Auction Status', uid: 'published' },
        { name: 'Bid Increment', uid: 'minimumBid' },
        { name: 'Current Bid', uid: 'currentBid' },
    ];

    // Get our list of owned auctions, where appResponse is AppResponse<List<Auction>>
    const { appResponse, error, status } = useHttpQuery('/auctions/mine');

    // Display a toast if we have an error
    useEffect(() => {
        if (
            error ||
            (status && status !== 200) ||
            (appResponse && !appResponse.success)
        ) {
            toast.custom((t) => (
                <Toast
                    message={
                        appResponse?.msg ||
                        'Failed to find owned auctions! Try again later'
                    }
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    const navigate = useNavigate();

    function mapItemValue(item, columnKey) {
        if (columnKey === 'published') {
            return item[columnKey] ? 'Published' : 'Not Published';
        } else if (columnKey === 'title') {
            return (
                <Link onPress={() => navigate('/auction/' + item.id)}>
                    {item[columnKey]}
                </Link>
            );
        } else if (columnKey.includes('Bid')) {
            return `$${item[columnKey].toFixed(2)}`;
        }
        return item[columnKey];
    }

    return (
        <Flex
            margin="auto"
            marginTop="size-400"
            height="80%"
            width="60%"
            direction="column"
            gap="size-200"
            data-testid="auctions-area"
        >
            <Heading level={1} alignSelf="center">
                Your Auctions
            </Heading>
            <TableView
                width="100%"
                selectionMode="none"
                aria-label="bid history"
                height="size-3000"
                renderEmptyState={() => <em>No Bids</em>}
            >
                <TableHeader columns={columns}>
                    {(column) => (
                        <Column key={column.uid}>{column.name}</Column>
                    )}
                </TableHeader>
                <TableBody
                    items={
                        appResponse && appResponse.success
                            ? appResponse.data
                            : []
                    }
                >
                    {(item) => (
                        <Row>
                            {(columnKey) => (
                                <Cell>{mapItemValue(item, columnKey)}</Cell>
                            )}
                        </Row>
                    )}
                </TableBody>
            </TableView>
        </Flex>
    );
};
