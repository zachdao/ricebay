import React, { useState } from 'react';
import PublishCheck from '@spectrum-icons/workflow/PublishCheck';
import {
    ActionButton,
    Badge,
    Button,
    Dialog,
    Flex,
    StatusLight,
    Text,
    DialogTrigger,
    Content,
} from '@adobe/react-spectrum';
import Cancel from '@spectrum-icons/workflow/Cancel';
import Copy from '@spectrum-icons/workflow/Copy';
import User from '@spectrum-icons/workflow/User';

export const EditState = ({
    auction,
    published,
    setPublished,
    cancelAuction,
    copyAuction,
    markPaid,
    isClean,
}) => {
    return (
        <Flex
            gridArea="status"
            direction="row"
            alignItems="center"
            justifyContent="start"
        >
            {auction && published === false && auction.bids?.length === 0 && (
                <RelistAuction onPress={() => setPublished(true)} />
            )}
            {auction &&
                auction.published === false &&
                auction.bids?.length > 0 && (
                    <CopyAuction onPress={() => copyAuction()} />
                )}
            {auction && auction.published === true && (
                <CancelAuction
                    isDisabled={auction?.bids?.length > 0 || !isClean()}
                    onPress={() => {
                        setPublished(false);
                        cancelAuction({ id: auction?.id, published: false });
                    }}
                />
            )}
            {auction && (
                <AuctionState
                    auctionId={auction?.id}
                    isPublished={auction?.published}
                    winner={auction?.winner}
                    markPaid={markPaid}
                    isRelisted={
                        auction?.published === false && published === true
                    }
                />
            )}
        </Flex>
    );
};

const RelistAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <PublishCheck />
            <Text>Re-list</Text>
        </Button>
    );
};

const CancelAuction = ({ onPress, isDisabled }) => {
    return (
        <Button
            variant="primary"
            maxWidth="max-content"
            onPress={onPress}
            isDisabled={isDisabled}
        >
            <Cancel />
            <Text>Cancel Auction</Text>
        </Button>
    );
};

const CopyAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <Copy />
            <Text>Copy Auction</Text>
        </Button>
    );
};

const AuctionState = ({
    auctionId,
    isPublished,
    winner,
    markPaid,
    isRelisted,
}) => {
    const [isOpen, setIsOpen] = useState(false);
    let variant = 'positive';
    let statusText = `Published ${!winner ? '(No Bids)' : ''}`;
    if (isRelisted) {
        variant = 'neutral';
        statusText = 'Unpublished (Save to Publish)';
    } else if (!isPublished && !winner) {
        variant = 'negative';
        statusText = 'Expired';
    } else if (!isPublished && winner) {
        variant = winner.hasPaid ? 'info' : 'notice';
        statusText = `Sold for $${winner.amount.toFixed(2)}`;
    }
    return (
        <Flex direction="row" alignItems="center">
            <StatusLight variant={variant}>{statusText}</StatusLight>
            {winner?.hasPaid === true ? (
                <BuyerBadge winner={winner} />
            ) : winner ? (
                <DialogTrigger
                    type="popover"
                    containerPadding="size-100"
                    isOpen={isOpen}
                    onOpenChange={setIsOpen}
                >
                    <ActionButton isQuiet>
                        <BuyerBadge winner={winner} />
                    </ActionButton>
                    <Dialog size="S">
                        <Content>
                            <Button
                                width="100%"
                                variant="cta"
                                onPress={() => {
                                    setIsOpen(false);
                                    markPaid({
                                        id: auctionId,
                                        winner: { ...winner, hasPaid: true },
                                    });
                                }}
                            >
                                Mark Paid
                            </Button>
                        </Content>
                    </Dialog>
                </DialogTrigger>
            ) : null}
        </Flex>
    );
};

const BuyerBadge = ({ winner }) => {
    return (
        <Badge
            variant={winner?.hasPaid ? 'positive' : 'negative'}
            marginStart="size-100"
        >
            <User />
            <Text>Buyer: {winner?.alias}</Text>
        </Badge>
    );
};
